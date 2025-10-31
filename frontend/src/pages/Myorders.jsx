import React, { useContext, useEffect, useState } from "react";
import { shopContext } from "../context/ShopContext";
import Title from "../components/Title";
import axios from "axios";

function Myorders() {
  const { currency, backendUrl, token } = useContext(shopContext);
  const [orderData, setOrderData] = useState([]);

  const loadOrderData = async () => {
    try {
      if (!token) {
        console.warn("No token found in context/localStorage");
        return;
      }

      console.log("Token sent to backend:", token);
      const response = await axios.get(`${backendUrl}/api/v1/order/user`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      console.log("Order API response:", response.data);

      // ✅ response.data is a plain array of orders
      if (Array.isArray(response.data)) {
        let allOrdersItem = [];

        response.data.forEach((order) => {
          order.orderItems.forEach((item) => {
            allOrdersItem.push({
              orderId: order.id,
              orderDate: order.orderDate,
              status: order.status,
              totalAmount: order.totalAmount,
              shippingAddress: order.shippingAddress,
              billingAddress: order.billingAddress,
              productId: item.productId,
              productName: item.productName,
              quantity: item.quantity,
              price: item.priceAtPurchase,
            });
          });
        });

        // Reverse to show latest orders first
        setOrderData(allOrdersItem.reverse());
      } else {
        console.warn("Unexpected order response format:", response.data);
      }
    } catch (err) {
      console.error("Error fetching orders:", err);
    }
  };

  useEffect(() => {
    loadOrderData();
  }, []);

  return (
    <div className="border-t pt-16">
      <div className="text-2xl">
        <Title text1={"MY"} text2={"ORDERS"} />
      </div>
      <div>
        {orderData.length === 0 ? (
          <p className="text-center text-gray-500">No orders found.</p>
        ) : (
          orderData.map((item, index) => (
            <div
              key={index}
              className="py-4 border-t border-b text-gray-700 flex flex-col md:flex-row md:items-center md:justify-between gap-4"
            >
              <div className="flex flex-col sm:flex-row sm:items-start gap-6 text-sm">
                {/* No product image in backend, so placeholder */}
                <img
                  src="/placeholder-image.png"
                  alt={item.productName}
                  className="w-16 sm:w-20 rounded"
                />
                <div>
                  <p className="sm:text-base font-medium">
                    {item.productName}
                  </p>
                  <div className="flex flex-wrap items-center gap-3 mt-1 text-base text-gray-700">
                    <p>
                      {currency}
                      {item.price}
                    </p>
                    <p>Quantity: {item.quantity}</p>
                  </div>
                  <p className="mt-1 text-sm">
                    Date:{" "}
                    <span className="text-gray-500">
                      {new Date(item.orderDate).toLocaleDateString()}
                    </span>
                  </p>
                  <p className="mt-1 text-sm">
                    Shipping:{" "}
                    <span className="text-gray-500">
                      {item.shippingAddress}
                    </span>
                  </p>
                  <p className="mt-1 text-sm">
                    Billing:{" "}
                    <span className="text-gray-500">
                      {item.billingAddress}
                    </span>
                  </p>
                </div>
              </div>

              <div className="md:w-1/3 flex justify-between items-center">
                <div className="flex items-center gap-2">
                  <span
                    className={`w-2 h-2 rounded-full ${
                      item.status === "PAID"
                        ? "bg-green-500"
                        : "bg-yellow-500"
                    }`}
                  ></span>
                  <p className="capitalize">{item.status}</p>
                </div>
                <button
                  onClick={() => alert(`Tracking order #${item.orderId}`)}
                  className="border px-4 py-2 text-sm font-medium rounded-sm hover:bg-gray-100 transition"
                >
                  Track Order
                </button>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default Myorders;
