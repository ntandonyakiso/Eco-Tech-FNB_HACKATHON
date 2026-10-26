import { useState, useEffect } from 'react';
import API from '../services/api';

export default function StockLossPage() {
  const [losses, setLosses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [form, setForm] = useState({ item: '', quantity: '', unitCost: '', lossReason: '', recordedAt: '' });

  useEffect(() => {
    load();
  }, []);

  const load = async () => {
    const data = await API.listStockLosses();
    setLosses(data.content || data);
    setLoading(false);
  };

  const add = async (e) => {
    e.preventDefault();
    await API.logStockLoss({
      item: form.item,
      quantity: Number(form.quantity) || 0,
      unitCost: Number(form.unitCost) || 0,
      lossReason: form.lossReason,
      recordedAt: form.recordedAt || new Date().toISOString(),
    });
    setForm({ item: '', quantity: '', unitCost: '', lossReason: '', recordedAt: '' });
    load();
  };

  if (loading) return <div className="p-4">Loading stock losses...</div>;

  return (
    <div className="min-h-screen p-2 md:p-4">
      <div className="mb-6">
        <h1 className="text-xl font-bold">Stock losses</h1>
        <p className="text-sm text-gray-600">Log spoiled stock after outages to track losses.</p>
      </div>

      <div className="bg-white p-4 rounded border shadow-sm mb-6">
        <form onSubmit={add} className="grid md:grid-cols-3 gap-3">
          <input name="item" placeholder="Item" defaultValue={form.item} className="border rounded px-2 py-1 text-sm w-full" required />
          <input name="quantity" type="number" step="0.01" placeholder="Qty" defaultValue={form.quantity} className="border rounded px-2 py-1 text-sm w-full" required />
          <input name="unitCost" type="number" step="0.01" placeholder="Unit cost (R)" defaultValue={form.unitCost} className="border rounded px-2 py-1 text-sm w-full" required />
          <input name="lossReason" placeholder="Reason" defaultValue={form.lossReason} className="border rounded px-2 py-1 text-sm w-full" required />
          <input name="recordedAt" type="datetime-local" defaultValue={form.recordedAt} className="border rounded px-2 py-1 text-sm w-full" required />
          <button className="bg-green-600 text-white px-3 py-1 rounded text-sm">Log loss</button>
        </form>
      </div>

      <h2 className="text-sm font-bold mb-2">Loss history</h2>
      <div className="overflow-x-auto">
        <table className="w-full text-sm border">
          <thead>
            <tr>
              <th className="border p-2 text-left">Item</th>
              <th className="border p-2 text-left">Qty</th>
              <th className="border p-2 text-left">Unit cost</th>
              <th className="border p-2 text-left">Total</th>
              <th className="border p-2 text-left">Reason</th>
              <th className="border p-2 text-left">Recorded</th>
            </tr>
          </thead>
          <tbody>
            {losses.map((l) => (
              <tr key={l.id} className="border">
                <td className="border p-2">{l.item}</td>
                <td className="border p-2">{l.quantity}</td>
                <td className="border p-2">R{l.unitCost}</td>
                <td className="border p-2">R{l.totalValue}</td>
                <td className="border p-2">{l.lossReason}</td>
                <td className="border p-2">{l.recordedAt ? new Date(l.recordedAt).toLocaleString() : '—'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
