import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { WardProvider } from './context/WardContext';
import ProtectedRoute from './components/ProtectedRoute';
import HomePage from './pages/HomePage';
import RegisterPage from './pages/RegisterPage';
import LoginPage from './pages/LoginPage';
import ReportCreatePage from './pages/ReportCreatePage';
import ReportsMapPage from './pages/ReportsMapPage';
import ReportDetailPage from './pages/ReportDetailPage';
import AlertsPage from './pages/AlertsPage';
import ResourceListPage from './pages/ResourceListPage';
import ResourceDetailPage from './pages/ResourceDetailPage';
import ResourceCreatePage from './pages/ResourceCreatePage';
import MyListingsPage from './pages/MyListingsPage';
import MyRequestsPage from './pages/MyRequestsPage';
import VulnerableContactPage from './pages/VulnerableContactPage';
import UsagePage from './pages/UsagePage';
import BusinessProfilePage from './pages/BusinessProfilePage';
import StockLossPage from './pages/StockLossPage';
import FoodPointsPage from './pages/FoodPointsPage';
import SurplusPostsPage from './pages/SurplusPostsPage';
import PreparednessPage from './pages/PreparednessPage';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <WardProvider>
          <Routes>
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/" element={<ProtectedRoute />}>
              <Route index element={<HomePage />} />
              <Route path="reports" element={<ReportsMapPage />} />
              <Route path="reports/new" element={<ReportCreatePage />} />
              <Route path="reports/:id" element={<ReportDetailPage />} />
              <Route path="alerts" element={<AlertsPage />} />
              <Route path="resources" element={<ResourceListPage />} />
              <Route path="resources/new" element={<ResourceCreatePage />} />
              <Route path="resources/:id" element={<ResourceDetailPage />} />
              <Route path="resources/my-listings" element={<MyListingsPage />} />
              <Route path="resources/my-requests" element={<MyRequestsPage />} />
              <Route path="vulnerable-contacts" element={<VulnerableContactPage />} />
              <Route path="usage" element={<UsagePage />} />
              <Route path="business-profile" element={<BusinessProfilePage />} />
              <Route path="stock-loss" element={<StockLossPage />} />
              <Route path="food-points" element={<FoodPointsPage />} />
              <Route path="surplus-posts" element={<SurplusPostsPage />} />
              <Route path="preparedness" element={<PreparednessPage />} />
            </Route>
          </Routes>
        </WardProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
