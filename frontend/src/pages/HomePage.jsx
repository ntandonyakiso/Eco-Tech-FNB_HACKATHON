import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useWards } from '../context/WardContext';
import BottomNavigation from '../components/BottomNavigation';

export default function HomePage() {
  const { user, logout } = useAuth();
  const { wards, loading } = useWards();
  const navigate = useNavigate();
  const [selectedWard, setSelectedWard] = useState(null);

  useEffect(() => {
    if (user.wardId && wards.length) {
      const w = wards.find((wd) => wd.id === user.wardId);
      if (w) setSelectedWard(w);
    }
  }, [user, wards]);

  if (loading) return <div className="p-4">Loading wards...</div>;

  return (
    <div className="min-h-screen bg-gray-50 pb-24">
      <header className="bg-white border-b px-4 py-4 shadow-sm sticky top-0 z-40">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-xl font-bold text-gray-900">Eco-Tech</h1>
            <p className="text-sm text-gray-500">Community disruption preparedness</p>
          </div>
          <div className="flex items-center gap-2">
            <span className="text-xs bg-gray-100 text-gray-700 px-2 py-1 rounded">
              {user?.role}
            </span>
            <button
              onClick={logout}
              className="text-sm text-red-600 font-medium p-1 hover:bg-red-50 rounded"
            >
              Logout
            </button>
          </div>
        </div>
      </header>

      <div className="px-4 space-y-4">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 mb-4">
          <div className="bg-white p-4 rounded-xl border shadow-sm">
            <h3 className="font-semibold text-sm mb-2">Your ward</h3>
            <p className="text-sm font-medium">{selectedWard ? (selectedWard.wardNumber ? 'Ward ' + selectedWard.wardNumber : '') + ' ' + selectedWard.name : 'None'}</p>
            <p className="text-xs text-gray-500">
              {selectedWard ? selectedWard.province + ', ' + selectedWard.municipality : ''}
            </p>
          </div>
          <div className="bg-white p-4 rounded-xl border shadow-sm">
            <h3 className="font-semibold text-sm mb-2">Language</h3>
            <p className="text-sm font-medium">{user?.language || 'en'}</p>
          </div>
          <div className="bg-white p-4 rounded-xl border shadow-sm">
            <h3 className="font-semibold text-sm mb-2">Role</h3>
            <p className="text-sm font-medium capitalize">{user?.role}</p>
          </div>
        </div>

        <section className="bg-white rounded-xl border shadow-sm overflow-hidden">
          <div className="px-4 py-3 border-b">
            <h2 className="text-lg font-bold text-gray-900">All wards</h2>
          </div>
          <div className="space-y-1">
            {wards.map((w) => (
              <button
                key={w.id}
                onClick={() => setSelectedWard(w)}
                className="w-full flex items-center justify-between px-4 py-3 border-b hover:bg-gray-50 text-left"
              >
                <div className="flex-1">
                  <div className="text-sm font-medium text-gray-900">
                    {w.wardNumber ? 'Ward ' + w.wardNumber : ''} {w.name}
                  </div>
                  <div className="text-xs text-gray-500">
                    {w.province} · {w.municipality}
                  </div>
                </div>
                {selectedWard?.id === w.id ? (
                  <span className="text-xs bg-green-100 text-green-700 px-2 py-0.5 rounded">
                    Selected
                  </span>
                ) : (
                  <span className="text-xs text-green-600 font-medium">Select</span>
                )}
              </button>
            ))}
          </div>
        </section>

        <nav className="mt-4 grid grid-cols-2 gap-3">
          <Link
            to="/reports"
            className="bg-green-600 text-white py-3 rounded-xl text-center font-medium text-sm shadow-sm hover:bg-green-700 transition-colors"
          >
            View reports
          </Link>
          <Link
            to="/resources"
            className="border border-gray-200 text-gray-700 py-3 rounded-xl text-center font-medium text-sm hover:bg-gray-50 transition-colors"
          >
            Resource share
          </Link>
        </nav>
      </div>

      <BottomNavigation />
    </div>
  );
}
