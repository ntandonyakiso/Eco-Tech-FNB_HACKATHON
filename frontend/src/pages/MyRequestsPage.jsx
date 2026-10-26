import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import API from '../services/api';

export default function MyRequestsPage() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    API.myRequests()
      .then(setRequests)
      .catch((e) => setError(e.message || 'Failed to load'))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="p-4">Loading...</div>;

  return (
    <div className="min-h-screen p-2 md:p-4">
      <Link to="/resources" className="text-sm text-green-600 mb-4 inline-block">← Back</Link>
      <h1 className="text-xl font-bold mb-6">My requests</h1>
      {error && <p className="mb-4 text-sm text-red-600">{error}</p>}
      {requests.length === 0 ? (
        <p className="text-sm text-gray-600">You haven't made any requests yet.</p>
      ) : (
        <div className="space-y-3">
          {requests.map((r) => (
            <div key={r.id} className="bg-white p-4 rounded border shadow-sm">
              <p className="text-sm">{r.message || '(no message)'}</p>
              <p className="text-xs text-gray-600">Status: {r.status}</p>
              <p className="text-xs text-gray-500">Requested: {r.createdAt ? new Date(r.createdAt).toLocaleDateString() : '—'}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
