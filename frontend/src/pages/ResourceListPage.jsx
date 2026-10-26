import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import API from '../services/api';

const CATEGORIES = ['WATER', 'SOLAR_CHARGING', 'GENERATOR', 'TRANSPORT', 'OTHER'];

export default function ResourceListPage() {
  const [listings, setListings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [category, setCategory] = useState('');
  const [nav, setNav] = useState(null);

  useEffect(() => {
    load();
  }, [category]);

  const load = async () => {
    setLoading(true);
    const params = category ? { category } : {};
    const data = await API.listResources(params);
    setListings(data.content);
    setLoading(false);
  };

  if (loading) return <div className="p-4">Loading resources...</div>;

  return (
    <div className="min-h-screen p-2 md:p-4">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-xl font-bold">Resource share</h1>
        <Link to="/resources/new" className="px-3 py-1 bg-green-600 text-white rounded text-sm">Post an offer</Link>
      </div>

      <div className="flex gap-2 mb-6">
        <button onClick={() => setCategory('')} className="px-3 py-1 border rounded text-sm bg-white">All</button>
        {CATEGORIES.map((c) => (
          <button key={c} onClick={() => setCategory(c)} className="px-3 py-1 border rounded text-sm">
            {c}
          </button>
        ))}
      </div>

      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
        {listings.map((l) => (
          <div key={l.id} className="bg-white p-4 rounded border shadow-sm">
            <div className="flex justify-between mb-2">
              <span className="font-semibold text-sm">{l.title}</span>
              <span className="text-xs px-1.5 py-0.5 rounded bg-blue-100 text-blue-700">{l.category}</span>
            </div>
            <p className="text-sm text-gray-700 mb-3">{l.description}</p>
            <p className="text-xs text-gray-500 mb-2">{l.contactMethod}: {l.contactValue}</p>
            <p className="text-xs text-gray-600 mb-2">Available: {l.availabilityStart ? new Date(l.availabilityStart).toLocaleDateString() : '—'} to {l.availabilityEnd ? new Date(l.availabilityEnd).toLocaleDateString() : '—'}</p>
            <Link to={"/resources/" + l.id} className="block px-3 py-1 bg-green-600 text-white rounded text-sm text-center">View details</Link>
          </div>
        ))}
      </div>

      <p className="mt-6 text-sm text-gray-600">
        <Link to="/" className="text-green-600">← Home</Link>
      </p>
    </div>
  );
}
