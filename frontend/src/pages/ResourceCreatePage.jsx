import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import API from '../services/api';

export default function ResourceCreatePage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    wardId: '',
    category: 'WATER',
    title: '',
    description: '',
    latitude: '',
    longitude: '',
    availabilityStart: '',
    availabilityEnd: '',
    contactMethod: 'phone',
    contactValue: '',
  });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const CATEGORIES = ['WATER', 'SOLAR_CHARGING', 'GENERATOR', 'TRANSPORT', 'OTHER'];
  const METHODS = ['phone', 'whatsapp'];

  const save = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await API.createResource({
        wardId: form.wardId || null,
        category: form.category,
        title: form.title,
        description: form.description,
        latitude: form.latitude ? parseFloat(form.latitude) : null,
        longitude: form.longitude ? parseFloat(form.longitude) : null,
        availabilityStart: form.availabilityStart || null,
        availabilityEnd: form.availabilityEnd || null,
        contactMethod: form.contactMethod,
        contactValue: form.contactValue,
      });
      navigate('/resources');
    } catch (e) {
      setError(e.message || 'Failed to create listing');
      setSubmitting(false);
    }
  };

  return (
    <div className="min-h-screen p-2 md:p-4">
      <Link to="/resources" className="text-sm text-green-600 mb-4 inline-block">← Back</Link>
      <h1 className="text-xl font-bold mb-6">Post a resource offer</h1>
      <form onSubmit={save} className="bg-white p-4 rounded border shadow-sm max-w-md">
        <div className="mb-3">
          <label className="block text-xs font-medium mb-1">Category</label>
          <select
            className="border rounded px-2 py-1 text-sm w-full"
            value={form.category}
            onChange={(e) => setForm({ ...form, category: e.target.value })}
          >
            {CATEGORIES.map((c) => (<option key={c} value={c}>{c}</option>))}
          </select>
        </div>
        <div className="mb-3">
          <label className="block text-xs font-medium mb-1">Title</label>
          <input
            className="border rounded px-2 py-1 text-sm w-full"
            value={form.title}
            onChange={(e) => setForm({ ...form, title: e.target.value })}
          />
        </div>
        <div className="mb-3">
          <label className="block text-xs font-medium mb-1">Description</label>
          <textarea
            className="border rounded px-2 py-1 text-sm w-full"
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
          />
        </div>
        <div className="mb-3">
          <label className="block text-xs font-medium mb-1">Latitude</label>
          <input
            type="number" step="any"
            className="border rounded px-2 py-1 text-sm w-full"
            value={form.latitude}
            onChange={(e) => setForm({ ...form, latitude: e.target.value })}
          />
        </div>
        <div className="mb-3">
          <label className="block text-xs font-medium mb-1">Longitude</label>
          <input
            type="number" step="any"
            className="border rounded px-2 py-1 text-sm w-full"
            value={form.longitude}
            onChange={(e) => setForm({ ...form, longitude: e.target.value })}
          />
        </div>
        <div className="mb-3">
          <label className="block text-xs font-medium mb-1">Available from</label>
          <input
            type="datetime-local"
            className="border rounded px-2 py-1 text-sm w-full"
            value={form.availabilityStart}
            onChange={(e) => setForm({ ...form, availabilityStart: e.target.value })}
          />
        </div>
        <div className="mb-3">
          <label className="block text-xs font-medium mb-1">Available until</label>
          <input
            type="datetime-local"
            className="border rounded px-2 py-1 text-sm w-full"
            value={form.availabilityEnd}
            onChange={(e) => setForm({ ...form, availabilityEnd: e.target.value })}
          />
        </div>
        <div className="mb-3">
          <label className="block text-xs font-medium mb-1">Contact method</label>
          <select
            className="border rounded px-2 py-1 text-sm w-full"
            value={form.contactMethod}
            onChange={(e) => setForm({ ...form, contactMethod: e.target.value })}
          >
            {METHODS.map((m) => (<option key={m} value={m}>{m}</option>))}
          </select>
        </div>
        <div className="mb-3">
          <label className="block text-xs font-medium mb-1">Contact value (phone / WhatsApp)</label>
          <input
            className="border rounded px-2 py-1 text-sm w-full"
            value={form.contactValue}
            onChange={(e) => setForm({ ...form, contactValue: e.target.value })}
          />
        </div>
        {error && <p className="mb-3 text-sm text-red-600">{error}</p>}
        <button
          type="submit"
          disabled={submitting}
          className="px-3 py-1 bg-green-600 text-white rounded text-sm"
        >
          {submitting ? 'Posting...' : 'Post offer'}
        </button>
      </form>
    </div>
  );
}
