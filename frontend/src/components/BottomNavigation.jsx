import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const NAV_ITEMS = [
  { to: '/', label: 'Home', icon: '🏠' },
  { to: '/reports', label: 'Reports', icon: '📋' },
  { to: '/alerts', label: 'Alerts', icon: '🔔' },
  { to: '/resources', label: 'Resources', icon: '📦' },
];

export default function BottomNavigation() {
  const { user, logout } = useAuth();
  const location = useLocation();
  const isAuthPage = location.pathname === '/login' || location.pathname === '/register';

  if (isAuthPage || !user) return null;

  return (
    <nav className="fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 px-4 pb-4 pt-3 shadow-lg z-50">
      <div className="max-w-md mx-auto flex justify-around gap-4">
        {NAV_ITEMS.map((item) => {
          const isActive = location.pathname === item.to || location.pathname.startsWith(item.to + '/');
          return (
            <Link
              key={item.to}
              to={item.to}
              className={"flex flex-col items-center gap-1 p-2 rounded-md transition-colors duration-200 " + (isActive ? 'bg-green-50 text-green-600' : 'text-gray-500 hover:text-gray-700')}
            >
              <span className="text-xl">{item.icon}</span>
              <span className="text-xs font-medium">{item.label}</span>
            </Link>
          );
        })}
        <button
          onClick={logout}
          className="flex flex-col items-center gap-1 p-2 rounded-md transition-colors duration-200 text-gray-500 hover:text-red-600 hover:bg-red-50"
        >
          <span className="text-xl">🚪</span>
          <span className="text-xs font-medium">Logout</span>
        </button>
      </div>
    </nav>
  );
}
