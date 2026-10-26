import { useState, useEffect } from 'react';
import API from '../services/api';

export default function PreparednessPage() {
  const [plan, setPlan] = useState(null);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({ householdType: '', wardId: '', planData: '' });

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    try {
      const p = await API.getPreparednessPlan();
      setPlan(p);
      setForm(p ? { householdType: p.householdType || '', wardId: p.wardId || '', planData: p.planData || '' } : form);
    } catch (e) {
      setPlan(null);
    }
    setLoading(false);
  };

  const save = async (e) => {
    e.preventDefault();
    await API.savePreparednessPlan(form);
    load();
  };

  if (loading) return <div className="p-4">Loading preparedness plan...</div>;

  return (
    <div className="min-h-screen p-2 md:p-4">
      <div className="mb-6">
        <h1 className="text-xl font-bold">Preparedness plan</h1>
        <p className="text-sm text-gray-600">Save your household emergency plan so it is available offline.</p>
      </div>

      <div className="bg-white p-4 rounded border shadow-sm">
        <form onSubmit={save} className="space-y-3">
          <div>
            <label className="block text-xs text-gray-600 mb-1">Household type</label>
            <input name="householdType" defaultValue={form.householdType} className="border rounded px-2 py-1 text-sm w-full" required />
          </div>
          <div>
            <label className="block text-xs text-gray-600 mb-1">Ward ID</label>
            <input name="wardId" defaultValue={form.wardId} className="border rounded px-2 py-1 text-sm w-full" required />
          </div>
          <div>
            <label className="block text-xs text-gray-600 mb-1">Plan details</label>
            <textarea name="planData" rows="4" defaultValue={form.planData} className="border rounded px-2 py-1 text-sm w-full" required placeholder="Emergency contacts, meeting point, supplies…"></textarea>
          </div>
          <button className="bg-green-600 text-white px-3 py-1 rounded text-sm">Save plan</button>
        </form>
      </div>

      {plan && (
        <div className="mt-6 bg-white p-4 rounded border shadow-sm">
          <h2 className="text-sm font-bold mb-2">Saved plan</h2>
          <p>Household: {plan.householdType}</p>
          <p>Ward: {plan.wardId}</p>
          <p className="whitespace-pre-wrap">{plan.planData}</p>
        </div>
      )}
    </div>
  );
}
