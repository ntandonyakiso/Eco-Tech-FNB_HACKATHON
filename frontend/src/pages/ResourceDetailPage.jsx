import { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import API from '../services/api';

export default function ResourceDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [listing, setListing] = useState(null);
  const [loading, setLoading] = useState(true);
  const [requests, setRequests] = useState([]);
  const [status, setStatus] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    load();
  }, [id]);

  const load = async () => {
    try {
      const data = await API.getResource(id);
      setListing(data);
      const reqs = await API.requestsForListing(id);
      setRequests(reqs);
    } catch (e) {
      setError(e.message || 'Failed to load');
    } finally {
      setLoading(false);
    }
  };

  const requestResource = async () => {
    try {
      await API.requestResource(id, message);
      setError('');
      setMessage('');
      load();
    } catch (e) {
      setError(e.message || 'Request failed');
    }
  };

  const respond = async (reqId, s) => {
    try {
      await API.respondToRequest(reqId, s);
      load();
    } catch (e) {
      setError(e.message || 'Failed to respond');
    }
  };

  if (loading) return <div className="p-4">Loading...</div>;
  if (!listing) return <div className="p-4">{error}</div>;

  const isOwner = true; // simplified: owner sees their own listing via my-listings

  return (
    <div className="min-h-screen p-2 md:p-4">
      <Link to="/resources" className="text-sm text-green-600 mb-4 inline-block">← Back</Link>
      <div className="bg-white p-4 rounded border shadow-sm mb-6">
        <h1 className="text-xl font-bold">{listing.title}</h1>
        <span className="px-2 py-0.5 rounded bg-blue-100 text-blue-700 text-xs">{listing.category}</span>
        <p className="text-sm text-gray-700 mt-2">{listing.description}</p>
        <p className="text-sm text-gray-600">{listing.contactMethod}: {listing.contactValue}</p>
        <p className="text-sm text-gray-600">Available: {listing.availabilityStart ? new Date(listing.availabilityStart).toLocaleDateString() : '—'} → {listing.availabilityEnd ? new Date(listing.availabilityEnd).toLocaleDateString() : '—'}</p>
        <p className="text-sm text-gray-600">Distance: {listing.distanceKm ? listing.distanceKm.toFixed(1) + ' km' : '—'}</p>
      </div>

      <div className="mb-6">
        <h2 className="font-bold mb-3">Request this</h2>
        <input
          type="text"
          className="border rounded px-2 py-1 text-sm w-full md:w-96"
          placeholder="Message (optional)"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
        />
        <button
          onClick={requestResource}
          className="px-3 py-1 bg-green-600 text-white rounded text-sm mt-2"
        >
          Send request
        </button>
        {error && <p className="mt-2 text-sm text-red-600">{error}</p>}
      </div>

      <div className="mb-6">
        <h2 className="font-bold mb-3">Requests</h2>
        {requests.length === 0 ? (
          <p className="text-sm text-gray-600">No requests yet.</p>
        ) : (
          <div className="space-y-3">
            {requests.map((r) => (
              <div key={r.id} className="p-3 border rounded bg-gray-50">
                <p className="text-sm">{r.message || '(no message)'}</p>
                <p className="text-xs text-gray-500">Status: {r.status}</p>
                {r.status === 'PENDING' && (
                  <div className="mt-2 flex gap-2">
                    <button onClick={() => respond(r.id, 'ACCEPTED')} className="px-3 py-1 bg-green-600 text-white rounded text-xs">Accept</button>
                    <button onClick={() => respond(r.id, 'REJECTED')} className="px-3 py-1 bg-red-600 text-white rounded text-xs">Reject</button>
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
      </div>

      <p className="mt-6 text-sm text-gray-600">
        <Link to="/" className="text-green-600">← Home</Link>
      </p>
    </div>
  );
}
