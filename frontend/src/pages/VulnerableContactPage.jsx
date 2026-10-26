import { useState } from 'react';
import { Link } from 'react-router-dom';
import API from '../services/api';

export default function VulnerableContactPage() {
  const [form, setForm] = useState({ name: '', phone: '', wardId: '', consent: false });
  const [contacts, setContacts] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    API.listVulnerableContacts()
      .then(setContacts)
      .catch((e) => setError(e.message || 'Failed to load'))
      .finally(() => setLoading(false));
  }, []);

  const save = async (e) => {
    e.preventDefault();
    try {
      await API.createVulnerableContact({
        name: form.name,
        phone: form.phone,
        wardId: form.wardId || null,
        consent: form.consent,
      });
      setForm({ name: '', phone: '', wardId: '', consent: false });
      setError('');
      API.listVulnerableContacts().then(setContacts);
    } catch (e) {
      setError(e.message || 'Failed to save');
    }
  };

  const remove = async (id) => {
    if (!window.confirm('Remove this contact?')) return;
    try {
      await API.deleteVulnerableContact(id);
      setContacts(contacts.filter((c) => c.id !== id));
    } catch (e) {
      setError(e.message || 'Failed to remove');
    }
  };

  if (loading) return <div className="p-4">Loading...</div>;

  return (
    <div className="min-h-screen p-2 md:p-4">
      <Link to="/" className="text-sm text-green-600 mb-4 inline-block">← Home</Link>
      <h1 className="text-xl font-bold mb-6">Check on the elderly</h1>
      <form onSubmit={save} className="bg-white p-4 rounded border shadow-sm max-w-md mb-6">
        <div className="mb-3">
          <label className="block text-xs font-medium mb-1">Name</label>
          <input
            className="border rounded px-2 py-1 text-sm w-full"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
          />
        </div>
        <div className="mb-3">
          <label className="block text-xs font-medium mb-1">Phone</label>
          <input
            className="border rounded px-2 py-1 text-sm w-full"
            value={form.phone}
            onChange={(e) => setForm({ ...form, phone: e.target.value })}
          />
        </div>
        <div className="mb-3">
          <label className="block text-xs font-medium mb-1">Ward ID</label>
          <input
            className="border rounded px-2 py-1 text-sm w-full"
            value={form.wardId}
            onChange={(e) => setForm({ ...form, wardId: e.target.value })}
          />
        </div>
        <div className="mb-3 flex items-start">
          <input
            type="checkbox"
            checked={form.consent}
            onChange={(e) => setForm({ ...form, consent: e.target.checked })}
            className="mt-1"
          />
          <label className="ml-2 text-sm">I have consent from this person to share their contact for check-ins during outages</label>
        </div>
        {error && <p className="mb-3 text-sm text-red-600">{error}</p>}
        <button type="submit" className="px-3 py-1 bg-green-600 text-white rounded text-sm">Register contact</button>
      </form>

      <h2 className="font-bold mb-3">My contacts</h2>
      {contacts.length === 0 ? (
        <p className="text-sm text-gray-600">No contacts registered.</p>
      ) : (
        <div className="space-y-2">
          {contacts.map((c) => (
            <div key={c.id} className="flex justify-between items-center bg-white p-3 rounded border shadow-sm">
              <div>
                <p className="text-sm font-medium">{c.name}</p>
                <p className="text-xs text-gray-600">{c.phone}</p>
              </div>
              <button
                onClick={() => remove(c.id)}
                className="px-2 py-1 text-red-600 text-sm hover:underline"
              >
                Remove
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
