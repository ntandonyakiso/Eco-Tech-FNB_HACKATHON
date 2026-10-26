import { useState, useEffect } from 'react';
import API from '../services/api';

export default function BusinessProfilePage() {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [form, setForm] = useState({});

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    try {
      const p = await API.getBusinessProfile();
      setProfile(p);
      setForm(p ? { businessName: p.businessName || '', businessType: p.businessType || '', averageHourlyRevenue: p.averageHourlyRevenue || '', monthlyRent: p.monthlyRent || '', staffCount: p.staffCount || '' } : {});
    } catch (e) {
      setProfile(null);
    }
    setLoading(false);
  };

  const save = async (e) => {
    e.preventDefault();
    await API.upsertBusinessProfile(form);
    setEditing(false);
    load();
  };

  return (
    <div className="min-h-screen p-2 md:p-4">
      <div className="mb-6">
        <h1 className="text-xl font-bold">Business profile</h1>
        <p className="text-sm text-gray-600">Track your business costs and estimate outage losses.</p>
      </div>

      <div className="bg-white p-4 rounded border shadow-sm">
        <form onSubmit={save} className="grid md:grid-cols-2 gap-3">
          <div>
            <label className="block text-xs text-gray-600 mb-1">Business name</label>
            <input name="businessName" defaultValue={form.businessName || ''} className="border rounded px-2 py-1 text-sm w-full" required />
          </div>
          <div>
            <label className="block text-xs text-gray-600 mb-1">Type (spaza, salon, vendor…)</label>
            <input name="businessType" defaultValue={form.businessType || ''} className="border rounded px-2 py-1 text-sm w-full" required />
          </div>
          <div>
            <label className="block text-xs text-gray-600 mb-1">Avg hourly revenue (R)</label>
            <input name="averageHourlyRevenue" type="number" step="0.01" defaultValue={form.averageHourlyRevenue || ''} className="border rounded px-2 py-1 text-sm w-full" />
          </div>
          <div>
            <label className="block text-xs text-gray-600 mb-1">Monthly rent (R)</label>
            <input name="monthlyRent" type="number" step="0.01" defaultValue={form.monthlyRent || ''} className="border rounded px-2 py-1 text-sm w-full" />
          </div>
          <div>
            <label className="block text-xs text-gray-600 mb-1">Staff count</label>
            <input name="staffCount" type="number" defaultValue={form.staffCount || ''} className="border rounded px-2 py-1 text-sm w-full" />
          </div>
          <div className="md:col-span-2">
            <button type="submit" className="bg-green-600 text-white px-3 py-1 rounded text-sm">{editing ? 'Update' : 'Save profile'}</button>
            {profile && <button type="button" onClick={() => setEditing(true)} className="ml-2 border rounded px-2 py-1 text-sm">Edit</button>}
          </div>
        </form>
      </div>

      {profile && (
        <div className="mt-6 bg-white p-4 rounded border shadow-sm">
          <h2 className="text-sm font-bold mb-2">Profile</h2>
          <p>Name: {profile.businessName}</p>
          <p>Type: {profile.businessType}</p>
        </div>
      )}
    </div>
  );
}
