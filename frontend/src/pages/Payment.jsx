import React from "react";
import { useLocation } from "react-router-dom";

function Payment() {
  const location = useLocation();
  const cartData = location.state?.cartData || [];
  const currency = location.state?.currency || "usd";

  const handleCheckout = async () => {
    const res = await fetch("http://localhost:8080/api/v1/payment/create-session", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ items: cartData, currency }),
    });

    const data = await res.json();
    window.location.href = data.url; 
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen">
      <h1 className="text-2xl font-bold mb-4">Checkout</h1>
      <ul className="mb-4">
        {cartData.map((item, index) => (
          <li key={index}>
            {item.quantity} × {item._id} ({item.size})
          </li>
        ))}
      </ul>
      <button
        onClick={handleCheckout}
        className="bg-black text-white px-6 py-3 rounded"
      >
        Pay with Stripe
      </button>
    </div>
  );
}

export default Payment;
