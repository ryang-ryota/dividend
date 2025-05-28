import React, { useState } from 'react';

function App() {
  const [code, setCode] = useState('');
  const [dividend, setDividend] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const fetchDividend = async () => {
    setLoading(true);
    setError('');
    setDividend(null);
    try {
      const response = await fetch(`/api/dividend/${code}/annual/2025`);
      if (!response.ok) {
        throw new Error('データ取得に失敗しました');
      }
      const value = await response.json();
      setDividend(value);
    } catch (e: any) {
      setError(e.message);
    }
    setLoading(false);
  };

  return (
    <div style={{ maxWidth: 400, margin: '2rem auto' }}>
      <h2>2025年度 合計配当金予想額</h2>
      <input
        type="text"
        value={code}
        onChange={e => setCode(e.target.value)}
        placeholder="銘柄コードを入力"
        style={{ width: '70%', marginRight: 8 }}
      />
      <button onClick={fetchDividend} disabled={loading || !code}>
        検索
      </button>
      <div style={{ marginTop: 20 }}>
        {loading && <div>取得中...</div>}
        {error && <div style={{ color: 'red' }}>{error}</div>}
        {dividend !== null && (
          <div>
            <strong>{code} の2025年度合計配当金予想額：</strong>
            <span>{dividend} 円</span>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
