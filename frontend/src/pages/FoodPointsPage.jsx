import { useState, useEffect } from 'react';
import API from '../services/api';

const TYPES = ['GARDEN', 'FOOD_BANK', 'SOUP_KITCHEN', 'MARKET', 'OTHER'];

export default function FoodPointsPage() {
  const [points, setPoints] = useState([]);
  const [mine, setMine] = useState([]);
  const [loading, setLoading] = useState(true);
  const [tab, setTab] = useState('all');
  const [form, setForm] = useState({ name: '', type: 'GARDEN', description: '', latitude: '', longitude: '', wardId: '' });

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    const [all, my] = await Promise.all([API.listFoodPoints(), API.myFoodPoints()]);
    setPoints(all);
    setMine(my);
    setLoading(false);
  };

  const add = async (e) => {
    e.preventDefault();
    await API.addFoodPoint({
      name: form.name,
      type: form.type,
      description: form.description,
      latitude: Number(form.latitude) || 0,
      longitude: Number(form.longitude) || 0,
      wardId: form.wardId || undefined,
    });
    setForm({ name: '', type: 'GARDEN', description: '', latitude: '', longitude: '', wardId: '' });
    load();
  };

  if (loading) return <div className="p-4">Loading food points...</div>;

  const show = tab === 'mine' ? mine : points;

  return (
    <div className="min-h-screen p-2 md:p-4">
      <div className="mb-6">
        <h1 className="text-xl font-bold">Food points</h1>
        <p className="text-sm text-gray-600">Community gardens, food banks and soup kitchens.</p>
      </div>

      <div className="flex gap-2 mb-6">
        <button onClick={() => setTab('all')} className={`px-3 py-1 border rounded text-sm ${tab === 'all' ? 'bg-green-600 text-white' : ''}`}>All</button>
        <button onClick={() => setTab('mine')} className={`px-3 py-1 border rounded text-sm ${tab === 'mine' ? 'bg-green-600 text-white' : ''}`}>Mine</button>
      </div>

      <div className="bg-white p-4 rounded border shadow-sm mb-6">
        <form onSubmit={add} className="grid md:grid-cols-3 gap-3">
          <input name="name" placeholder="Name" defaultValue={form.name} className="border rounded px-2 py-1 text-sm w-full" required />
          <select name="type" defaultValue={form.type} className="border rounded px-2 py-1 text-sm w-full">
            {TYPES.map((t) => <option key={t} value={t}>{t}</option>)}
          </select>
          <input name="description" placeholder="Description" defaultValue={form.description} className="border rounded px-2 py-1 text-sm w-full" />
          <input name="latitude" type="number" step="any" placeholder="Lat" defaultValue={form.latitude} className="border rounded px-2 py-1 text-sm w-full" />
          <input name="longitude" type="number" step="any" placeholder="Lon" defaultValue={form.longitude} className="border rounded px-2 py-1 text-sm w-full" />
          <input name="wardId" placeholder="Ward ID" defaultValue={form.wardId} className="border rounded px-2 py-1 text-sm w-full" />
          <button className="bg-green-600 text-white px-3 py-1 rounded text-sm">Add food point</button>
        </form>
      </div>

      <h2 className="text-sm font-bold mb-2">{tab === 'mine' ? 'My food points' : 'All food points'}</h2>
      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
        {show.map((p) => (
          <div key={p.id} className="bg-white p-4 rounded border shadow-sm">
            <div className="flex justify-between mb-2">
              <span className="font-semibold text-sm">{p.name}</span>
              <span className="text-xs px-1.5 py-0.5 rounded bg-blue-100 text-blue-700">{p.type}</span>
            </div>
            <p className="text-sm text-gray-700 mb-3">{p.description}</p>
            <p className="text-xs text-gray-500">Lat: {p.latitude}, Lon: {p.longitude}</p>
            {tab === 'mine' && p.approved !== undefined && <p className="text-xs text-gray-400 mt-2">Approved: {p.approved ? 'yes' : 'pending'}</p>}
          </div>
        ))}
      </div>
    </div>
  );
}
