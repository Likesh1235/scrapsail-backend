# Admin Dashboard Fix Instructions

## Changes Needed in Frontend

### File: `scrapsail-frontend-new/src/pages/AdminDashboard.jsx`

## 1. Show All Orders (Including Assigned) in History

**Current Issue:** Only pending orders are shown. Assigned/approved orders disappear.

**Fix:** Fetch ALL orders using `/api/admin/all-orders` endpoint which returns all orders including assigned ones.

### Update the useEffect to fetch all orders:

```javascript
useEffect(() => {
  const fetchAllOrders = async () => {
    setLoading(true);
    try {
      // Fetch ALL orders (not just pending)
      const response = await fetch(`${API_CONFIG.SPRING_BOOT_URL}/api/admin/all-orders`);
      const data = await response.json();
      
      if (data.success) {
        // Separate pending from all orders
        const pending = data.orders.filter(o => o.status === 'PENDING_APPROVAL');
        const allOther = data.orders.filter(o => o.status !== 'PENDING_APPROVAL');
        
        setPendingPickups(pending);
        setAllOrders(data.orders); // Store all orders for history view
        setMessage(`✅ Loaded ${data.orders.length} total orders (${pending.length} pending)`);
      } else {
        setMessage("❌ Failed to load orders");
      }
    } catch (error) {
      console.error('Error fetching orders:', error);
      setMessage(`❌ Error: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  fetchAllOrders();
}, []);
```

### Add state for all orders:

```javascript
const [allOrders, setAllOrders] = useState([]);
```

### Update your table to show all orders (or create a separate "History" section):

```javascript
// In your render, add a section for "Order History" that shows all orders
<div className="mt-8">
  <h2 className="text-2xl font-bold text-white mb-4">Order History (All Orders)</h2>
  <div className="overflow-x-auto max-h-[500px] overflow-y-auto custom-scrollbar">
    <table className="min-w-full">
      <thead className="bg-white/20 sticky top-0 z-10">
        <tr>
          <th className="py-3 px-4 text-left text-white font-semibold">Order ID</th>
          <th className="py-3 px-4 text-left text-white font-semibold">Customer</th>
          <th className="py-3 px-4 text-left text-white font-semibold">Waste Type</th>
          <th className="py-3 px-4 text-left text-white font-semibold">Weight</th>
          <th className="py-3 px-4 text-left text-white font-semibold">Status</th>
          <th className="py-3 px-4 text-left text-white font-semibold">Collector</th>
          <th className="py-3 px-4 text-left text-white font-semibold">Date</th>
        </tr>
      </thead>
      <tbody>
        {allOrders.map((order) => (
          <tr key={order.id} className="border-b border-white/20 hover:bg-white/10">
            <td className="py-4 px-4 text-white">#{order.id}</td>
            <td className="py-4 px-4 text-white">{order.userEmail || 'N/A'}</td>
            <td className="py-4 px-4 text-white">{order.itemType || 'N/A'}</td>
            <td className="py-4 px-4 text-white">{order.weight || 0} kg</td>
            <td className="py-4 px-4">
              <span className={`px-3 py-1 rounded-full text-sm ${getStatusColor(order.status)}`}>
                {order.status}
              </span>
            </td>
            <td className="py-4 px-4 text-white">{order.collectorEmail || 'Not assigned'}</td>
            <td className="py-4 px-4 text-white text-sm">
              {order.createdAt ? new Date(order.createdAt).toLocaleDateString() : 'N/A'}
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  </div>
</div>
```

## 2. Add Scroll Button to Top

Add a "Scroll to Top" button that appears when you scroll down:

### Add state for scroll visibility:

```javascript
const [showScrollTop, setShowScrollTop] = useState(false);

useEffect(() => {
  const handleScroll = () => {
    setShowScrollTop(window.scrollY > 300);
  };
  
  window.addEventListener('scroll', handleScroll);
  return () => window.removeEventListener('scroll', handleScroll);
}, []);
```

### Add the scroll button component:

```javascript
{/* Scroll to Top Button */}
{showScrollTop && (
  <button
    onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}
    className="fixed bottom-8 right-8 bg-green-500 hover:bg-green-600 text-white rounded-full p-4 shadow-lg z-50 transition-all duration-300 animate-bounce"
    title="Scroll to top"
  >
    <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 10l7-7m0 0l7 7m-7-7v18" />
    </svg>
  </button>
)}
```

### Add CSS for smooth scrolling (in your index.css or component):

```css
html {
  scroll-behavior: smooth;
}
```

## 3. Ensure Tables Have Scrolling

Make sure your tables already have the custom scrollbar (which should already be there):

```javascript
<div className="overflow-x-auto max-h-[500px] overflow-y-auto custom-scrollbar">
  {/* Table content */}
</div>
```

The `custom-scrollbar` class should already be defined in your `index.css`.

## Summary

1. ✅ Change API call from `/api/admin/pending-orders` to `/api/admin/all-orders`
2. ✅ Store all orders in state (not just pending)
3. ✅ Display all orders in a "History" section below pending orders
4. ✅ Add scroll-to-top button
5. ✅ Ensure tables have max-height and scrolling enabled

## Backend Already Updated

The backend `/api/admin/all-orders` endpoint now:
- Returns ALL orders (including assigned, approved, completed)
- Sorts orders by date (newest first)
- Groups orders by status for easier filtering
- Returns counts for each status

You just need to update the frontend to use this endpoint and display all orders!

