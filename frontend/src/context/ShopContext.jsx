import { createContext, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import axios from "axios";

export const shopContext = createContext();

export const ShopContextProvider = ({ children }) => {
  const [cartItems, setCartItems] = useState({});
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const currency = "₹";
  const delivery_fee = 40;
  const navigate = useNavigate();
  const backendUrl = import.meta.env.VITE_BACKEND_URL;
  //const backendUrl = "http://localhost:8181";

    const [token, setToken] = useState(() => {
    return localStorage.getItem("token") || null;
  });

  useEffect(() => {
    if (token) {
      localStorage.setItem("token", token);
    } else {
      localStorage.removeItem("token");
    }
  }, [token]);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setLoading(true);
        setError(null);

        const response = await fetch(`${backendUrl}/api/v1/products/all`);
        const data = await response.json();
        console.log("Fetched products:", data);
        setProducts(data);
      } catch (err) {
        console.error("Error fetching products:", err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, [backendUrl]);

  const addToCart = async (productId, size = "M", quantity = 1) => {
  const key = productId.toString();
  setCartItems((prev) => {
    const updatedCart = { ...prev };
    updatedCart[key] = updatedCart[key] || {};
    updatedCart[key][size] = (updatedCart[key][size] || 0) + quantity;
    return updatedCart;
  });

  toast.success("Item Added To Cart");

  try {
    if (token) {
      await axios.post(
        `${backendUrl}/api/v1/cart/add`,
        { productId, quantity, size },
        { headers: { Authorization: `Bearer ${token}` } }
      );
    } else {
      console.warn("No token — skipping backend cart sync");
    }
  } catch (err) {
    console.error("Failed to sync cart:", err);
    toast.error("Could not sync cart with server");
  }
    
  };

  const updateQuantity = (productId, size, quantity) => {
    const key = productId.toString();
    setCartItems((prev) => {
      const updatedCart = { ...prev };
      if (!updatedCart[key]) return updatedCart;

      if (quantity <= 0) {
        delete updatedCart[key][size];
        if (Object.keys(updatedCart[key]).length === 0) delete updatedCart[key];
      } else {
        updatedCart[key][size] = quantity;
      }

      return updatedCart;
    });
  };

  const getCartAmount = () => {
    let total = 0;
    for (const productId in cartItems) {
      const numericId = Number(productId);
      const product = products.find((p) => p.id === numericId);
      if (!product) continue;

      for (const size in cartItems[productId]) {
        total += product.price * cartItems[productId][size];
      }
    }
    return total;
  };

  const getCartCount = () => {
    let count = 0;
    for (const productId in cartItems) {
      for (const size in cartItems[productId]) {
        count += cartItems[productId][size] || 0;
      }
    }
    return count;
  };

  return (
    <shopContext.Provider
      value={{
        products,
        cartItems,
        addToCart,
        setCartItems,
        updateQuantity,
        getCartAmount,
        getCartCount,
        navigate,
        currency,
        delivery_fee,
        token,
        setToken,
        loading,
        error,
        backendUrl
      }}
    >
      {children}
    </shopContext.Provider>
  );
};
