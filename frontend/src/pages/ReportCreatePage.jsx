import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import API from '../services/api';

const CATEGORIES = [
  { value: 'POTHOLE', label: 'Pothole' },
  { value: 'BURST_PIPE', label: 'Burst pipe' },
  { value: 'STREETLIGHT', label: 'Streetlight' },
  { value: 'OUTAGE', label: 'Power outage' },
  { value: 'OTHER', label: 'Other' },
];

export default function ReportCreatePage() {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [category, setCategory] = useState('POTHOLE');
  const [latitude, setLatitude] = useState('');
  const [longitude, setLongitude] = useState('');
  const [photo, setPhoto] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { user } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!latitude || !longitude) {
      setError('Please provide GPS coordinates');
      return;
    }
    const lat = parseFloat(latitude);
    const lon = parseFloat(longitude);
    if (isNaN(lat) || isNaN(lon)) {
      setError('Invalid coordinates');
      return;
    }

    setLoading(true);
    try {
      const formData = new FormData();
      const requestJson = JSON.stringify({
        title,
        description,
        category,
        latitude: lat,
        longitude: lon,
        wardId: user?.wardId,
      });
      formData.append('request', new Blob([requestJson], { type: 'application/json' }));
      if (photo) formData.append('photo', photo);

      await API.createReport(formData);
      navigate('/reports');
    } catch (err) {
      setError(err.message || 'Failed to create report');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-xl mx-auto p-4">
      <h1 className="text-2xl font-bold mb-6">Report a fault</h1>
      <form onSubmit={handleSubmit} className="space-y-4 bg-white p-6 rounded border shadow-sm">
        <div>
          <label className="block text-sm font-medium mb-1">Title</label>
          <input
            type="text"
            className="w-full border rounded px-3 py-2"
            placeholder="e.g. Large pothole on Main Road"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Description</label>
          <textarea
            className="w-full border rounded px-3 py-2"
            rows={3}
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Category</label>
          <select className="w-full border rounded px-3 py-2" value={category} onChange={(e) => setCategory(e.target.value)}>
            {CATEGORIES.map((c) => (
              <option key={c.value} value={c.value}>{c.label}</option>
            ))}
          </select>
        </div>
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium mb-1">Latitude</label>
            <input
              type="number"
              step="any"
              className="w-full border rounded px-3 py-2"
              placeholder="-29.7378"
              value={latitude}
              onChange={(e) => setLatitude(e.target.value)}
              required
            />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Longitude</label>
            <input
              type="number"
              step="any"
              className="w-full border rounded px-3 py-2"
              placeholder="30.9041"
              value={longitude}
              onChange={(e) => setLongitude(e.target.value)}
              required
            />
          </div>
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Photo (optional)</label>
          <input
            type="file"
            accept="image/*"
            className="w-full block text-sm"
            onChange={(e) => setPhoto(e.target.files?.[0] ?? null)}
          />
        </div>
        {error && <p className="text-sm text-red-600">{error}</p>}
        <button
          type="submit"
          disabled={loading}
          className="w-full bg-green-600 text-white py-2 rounded disabled:opacity-50"
        >
          {loading ? 'Submitting...' : 'Submit report'}
        </button>
      </form>
      <p className="text-center text-sm text-gray-600 mt-4">
        <a href="/reports" className="text-green-600">View all reports</a>
      </p>
    </div>
  );
}
