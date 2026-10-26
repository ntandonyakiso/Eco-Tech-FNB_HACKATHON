import { useState, useEffect } from 'react';
import API from '../services/api';

export default function SurplusPostsPage() {
  const [posts, setPosts] = useState([]);
  const [mine, setMine] = useState([]);
  const [loading, setLoading] = useState(true);
  const [tab, setTab] = useState('all');
  const [form, setForm] = useState({ title: '', description: '', pickupLocation: '', pickupStart: '', pickupEnd: '', expiresAt: '' });

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    const [all, my] = await Promise.all([API.listSurplusPosts(), API.mySurplusPosts()]);
    setPosts(all);
    setMine(my);
    setLoading(false);
  };

  const add = async (e) => {
    e.preventDefault();
    await API.createSurplusPost({
      title: form.title,
      description: form.description,
      pickupLocation: form.pickupLocation,
      pickupStart: form.pickupStart || new Date().toISOString(),
      pickupEnd: form.pickupEnd || new Date().toISOString(),
      expiresAt: form.expiresAt || new Date(Date.now() + 86400000).toISOString(),
    });
    setForm({ title: '', description: '', pickupLocation: '', pickupStart: '', pickupEnd: '', expiresAt: '' });
    load();
  };

  const claim = async (id) => {
    await API.claimSurplusPost(id);
    load();
  };

  if (loading) return <div className="p-4">Loading surplus posts...</div>;

  const show = tab === 'mine' ? mine : posts;

  return (
    <div className="min-h-screen p-2 md:p-4">
      <div className="mb-6">
        <h1 className="text-xl font-bold">Surplus food posts</h1>
        <p className="text-sm text-gray-600">Share surplus food with neighbours before it goes to waste.</p>
      </div>

      <div className="flex gap-2 mb-6">
        <button onClick={() => setTab('all')} className={`px-3 py-1 border rounded text-sm ${tab === 'all' ? 'bg-green-600 text-white' : ''}`}>All</button>
        <button onClick={() => setTab('mine')} className={`px-3 py-1 border rounded text-sm ${tab === 'mine' ? 'bg-green-600 text-white' : ''}`}>Mine</button>
      </div>

      <div className="bg-white p-4 rounded border shadow-sm mb-6">
        <form onSubmit={add} className="grid md:grid-cols-3 gap-3">
          <input name="title" placeholder="Title" defaultValue={form.title} className="border rounded px-2 py-1 text-sm w-full" required />
          <input name="description" placeholder="Description" defaultValue={form.description} className="border rounded px-2 py-1 text-sm w-full" required />
          <input name="pickupLocation" placeholder="Pickup location" defaultValue={form.pickupLocation} className="border rounded px-2 py-1 text-sm w-full" required />
          <input name="pickupStart" type="datetime-local" defaultValue={form.pickupStart} className="border rounded px-2 py-1 text-sm w-full" required />
          <input name="pickupEnd" type="datetime-local" defaultValue={form.pickupEnd} className="border rounded px-2 py-1 text-sm w-full" required />
          <input name="expiresAt" type="datetime-local" defaultValue={form.expiresAt} className="border rounded px-2 py-1 text-sm w-full" required />
          <button className="bg-green-600 text-white px-3 py-1 rounded text-sm">Post surplus</button>
        </form>
      </div>

      <h2 className="text-sm font-bold mb-2">{tab === 'mine' ? 'My posts' : 'Available surplus'}</h2>
      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
        {show.map((p) => (
          <div key={p.id} className="bg-white p-4 rounded border shadow-sm">
            <div className="flex justify-between mb-2">
              <span className="font-semibold text-sm">{p.title}</span>
              <span className={`text-xs px-1.5 py-0.5 rounded ${p.claimed ? 'bg-gray-100 text-gray-600' : 'bg-green-100 text-green-700'}`}>{p.claimed ? 'Claimed' : 'Available'}</span>
            </div>
            <p className="text-sm text-gray-700 mb-3">{p.description}</p>
            <p className="text-xs text-gray-500 mb-2">{p.shopName}</p>
            <p className="text-xs text-gray-600 mb-2">Pickup: {new Date(p.pickupStart).toLocaleString()} — {new Date(p.pickupEnd).toLocaleString()}</p>
            {!p.claimed && (
              <button onClick={() => claim(p.id)} className="block w-full px-3 py-1 bg-green-600 text-white rounded text-sm text-center">Claim</button>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}
