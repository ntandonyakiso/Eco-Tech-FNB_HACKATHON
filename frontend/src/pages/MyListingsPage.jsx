import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import API from '../services/api';

export default function MyListingsPage() {
  const [listings, setListings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    API.myListings()
      .then(setListings)
      .catch((e) => setError(e.message || 'Failed to load'))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="p-4">Loading...</div>;

  return (
    <div className="min-h-screen p-2 md:p-4">
      <Link to="/resources" className="text-sm text-green-600 mb-4 inline-block">← Back</Link>
      <h1 className="text-xl font-bold mb-6">My listings</h1>
      {error && <p className="mb-4 text-sm text-red-600">{error}</p>}
      {listings.length === 0 ? (
        <p className="text-sm text-gray-600">You haven't posted any listings yet.</p>
      ) : (
        <div className="grid md:grid-cols-2 gap-4">
          {listings.map((l) => (
            <div key={l.id} className="bg-white p-4 rounded border shadow-sm">
              <div className="flex justify-between mb-2">
                <span className="font-semibold text-sm">{l.title}</span>
                <span className="text-xs px-1.5 py-0.5 rounded bg-blue-100 text-blue-700">{l.category}</span>
              </div>
              <p className="text-sm text-gray-700 mb-2">{l.description}</p>
              <p className="text-xs text-gray-600">{l.contactMethod}: {l.contactValue}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
