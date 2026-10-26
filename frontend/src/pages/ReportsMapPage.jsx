import { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import API from '../services/api';

// Fix Leaflet marker icons
const icon = L.icon({
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
});
L.Marker.prototype.options.icon = icon;

const STATUS_COLORS = {
  OPEN: '#ef4444',
  ACKNOWLEDGED: '#f59e0b',
  IN_PROGRESS: '#3b82f6',
  RESOLVED: '#22c55e',
};

export default function ReportsMapPage() {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('');

  useEffect(() => {
    API.listReports({ status: filter || undefined })
      .then(setReports)
      .catch(() => setReports([]))
      .finally(() => setLoading(false));
  }, [filter]);

  if (loading) return <div className="p-4">Loading reports...</div>;

  return (
    <div className="min-h-screen p-2 md:p-4">
      <div className="flex items-center justify-between mb-4">
        <h1 className="text-xl font-bold">Fault reports</h1>
        <div className="flex gap-2">
          <button
            onClick={() => setFilter('')}
            className={`px-3 py-1 rounded border text-sm ${filter === '' ? 'bg-green-600 text-white' : 'bg-white'}`}
          >
            All
          </button>
          {['OPEN', 'ACKNOWLEDGED', 'IN_PROGRESS', 'RESOLVED'].map((s) => (
            <button
              key={s}
              onClick={() => setFilter(s)}
              className={`px-3 py-1 rounded border text-sm ${filter === s ? 'bg-green-600 text-white' : 'bg-white'}`}
            >
              {s.toLowerCase().replace('_', ' ')}
            </button>
          ))}
        </div>
      </div>
      <div className="flex-1 min-h-[500px] border rounded overflow-hidden">
        <MapContainer center={[-29.7378, 30.9041]} zoom={13} style={{ height: '100%', width: '100%' }}>
          <TileLayer
            url='https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
            attribution='© OpenStreetMap contributors'
          />
          {reports.map((r) => (
            <Marker key={r.id} position={[r.latitude, r.longitude]}>
              <Popup>
                <div className="w-48">
                  <h3 className="font-semibold text-sm">{r.title}</h3>
                  <p className="text-xs text-gray-600">{r.category}</p>
                  <span
                    className="inline-block mt-1 px-2 py-0.5 rounded text-xs font-medium"
                    style={{ backgroundColor: STATUS_COLORS[r.status] || '#999', color: '#fff' }}
                  >
                    {r.status}
                  </span>
                </div>
              </Popup>
            </Marker>
          ))}
        </MapContainer>
      </div>
      <nav className="mt-4 flex gap-2">
        <a href="/reports/new" className="px-4 py-2 bg-green-600 text-white rounded">+ New report</a>
        <a href="/" className="px-4 py-2 border rounded text-gray-700">Home</a>
      </nav>
    </div>
  );
}
