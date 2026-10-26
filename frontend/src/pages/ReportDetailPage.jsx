import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import API from '../services/api';

const STATUS_COLORS = {
  OPEN: '#ef4444',
  ACKNOWLEDGED: '#f59e0b',
  IN_PROGRESS: '#3b82f6',
  RESOLVED: '#22c55e',
};

export default function ReportDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [report, setReport] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [userVoted, setUserVoted] = useState(false);

  useEffect(() => {
    API.getReport(id)
      .then((r) => {
        setReport(r);
        setUserVoted(r.userVoted ?? false);
      })
      .catch((err) => setError(err.message || 'Failed to load report'))
      .finally(() => setLoading(false));
  }, [id]);

  const handleUpvote = async () => {
    try {
      await API.upvoteReport(id);
      setUserVoted(true);
      if (report) setReport({ ...report, upvoteCount: (report.upvoteCount || 0) + 1, userVoted: true });
    } catch (err) {
      setError(err.message || 'Failed to upvote');
    }
  };

  if (loading) return <div className="p-4">Loading...</div>;
  if (error) return <div className="p-4 text-red-600">{error}</div>;

  return (
    <div className="max-w-3xl mx-auto p-4">
      <nav className="mb-4">
        <a href="/reports" className="text-sm text-green-600">← Back to map</a>
      </nav>
      <div className="bg-white p-6 rounded border shadow-sm">
        <div className="flex justify-between items-start mb-4">
          <div>
            <h1 className="text-xl font-bold">{report.title}</h1>
            <p className="text-sm text-gray-600">{report.category}</p>
          </div>
          <span
            className="inline-block px-3 py-1 rounded text-xs font-medium"
            style={{ backgroundColor: STATUS_COLORS[report.status] || '#999', color: '#fff' }}
          >
            {report.status}
          </span>
        </div>
        <p className="mb-4 text-gray-800">{report.description}</p>
        <div className="flex items-center gap-4 mb-6 text-sm">
          <div className="flex items-center gap-1">
            <span className="text-gray-500">↑</span>
            <span>{report.upvoteCount}</span>
          </div>
          {!userVoted ? (
            <button onClick={handleUpvote} className="px-3 py-1 bg-green-600 text-white rounded text-sm hover:opacity-90">
              Upvote
            </button>
          ) : (
            <span className="text-green-600 text-sm font-medium">Voted</span>
          )}
        </div>
        {report.photoUrl && (
          <div className="mb-6">
            <img
              src={report.photoUrl}
              alt="Report photo"
              className="max-w-full rounded border"
            />
          </div>
        )}
        <div>
          <h2 className="font-bold text-lg mb-3">Status history</h2>
          <div className="space-y-3">
            {(report.statusHistory || []).map((h) => (
              <div key={h.id} className="flex gap-3 text-sm">
                <span className="w-20 text-gray-600">{new Date(h.changedAt).toLocaleString()}</span>
                <span className="w-32">{h.oldStatus ? h.oldStatus + ' → ' : ''}{h.newStatus}</span>
                <span className="flex-1">{h.note || '-'}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
