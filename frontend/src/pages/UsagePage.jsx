import { useState, useEffect } from 'react';
import API from '../services/api';

export default function UsagePage() {
  const [loading, setLoading] = useState(true);
  const [summary, setSummary] = useState(null);
  const [logs, setLogs] = useState([]);

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    const [sum, list] = await Promise.all([API.usageSummary(), API.listUsage()]);
    setSummary(sum);
    setLogs(list.content || list);
    setLoading(false);
  };

  const add = async (e) => {
    e.preventDefault();
    const form = new FormData(e.target);
    const data = {
      type: form.get('type'),
      amount: Number(form.get('amount')),
      unit: form.get('unit') || '',
      recordedAt: form.get('recordedAt') || new Date().toISOString(),
    };
    await API.logUsage(data);
    e.target.reset();
    load();
  };

  if (loading) return <div className="p-4">Loading usage tracker...</div>;

  return (
    <div className="min-h-screen p-2 md:p-4">
      <div className="mb-6">
        <h1 className="text-xl font-bold">Usage tracker</h1>
        <p className="text-sm text-gray-600">Record water and energy use to see estimated savings.</p>
      </div>

      <div className="grid md:grid-cols-2 gap-4 mb-6">
        <div className="bg-white p-4 rounded border shadow-sm">
          <h2 className="text-sm font-bold mb-2">This week</h2>
          {summary ? (
            <div className="space-y-2">
              <p>Water: {summary.totalWater ? summary.totalWater.toFixed(2) : '—'}</p>
              <p>Energy: {summary.totalEnergy ? summary.totalEnergy.toFixed(2) : '—'}</p>
              <p className="font-bold">Estimated savings: R{summary.estimatedRandSavings ? summary.estimatedRandSavings.toFixed(2) : '0.00'}</p>
            </div>
          ) : <p className="text-gray-500 text-sm">—</p>}
        </div>
        <div className="bg-white p-4 rounded border shadow-sm">
          <h2 className="text-sm font-bold mb-2">Log usage</h2>
          <form onSubmit={add} className="space-y-2">
            <select name="type" className="border rounded px-2 py-1 text-sm w-full">
              <option value="WATER">Water</option>
              <option value="ENERGY">Energy</option>
            </select>
            <input name="amount" type="number" step="0.01" placeholder="Amount" className="border rounded px-2 py-1 text-sm w-full" required />
            <input name="unit" placeholder="Unit (e.g. kWh, kl)" className="border rounded px-2 py-1 text-sm w-full" />
            <input name="recordedAt" type="datetime-local" className="border rounded px-2 py-1 text-sm w-full" />
            <button className="bg-green-600 text-white px-3 py-1 rounded text-sm">Log</button>
          </form>
        </div>
      </div>

      <h2 className="text-sm font-bold mb-2">Recent logs</h2>
      <div className="overflow-x-auto">
        <table className="w-full text-sm border">
          <thead>
            <tr>
              <th className="border p-2 text-left">Type</th>
              <th className="border p-2 text-left">Amount</th>
              <th className="border p-2 text-left">Unit</th>
              <th className="border p-2 text-left">Recorded</th>
            </tr>
          </thead>
          <tbody>
            {logs.map((l) => (
              <tr key={l.id} className="border">
                <td className="border p-2">{l.type}</td>
                <td className="border p-2">{l.amount}</td>
                <td className="border p-2">{l.unit}</td>
                <td className="border p-2">{l.recordedAt ? new Date(l.recordedAt).toLocaleString() : '—'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
