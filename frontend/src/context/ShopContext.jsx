import { createContext, useState } from "react";
import { products as productData } from "../assets/assets";
import { useNavigate } from "react-router-dom";

export const shopContext = createContext();

export const ShopContextProvider = ({ children }) => {
  const [cartItems, setCartItems] = useState({}); 
  const [products] = useState(productData);
  const [token, setToken] = useState(null); 
  const currency = "₹";
  const delivery_fee = 40;
  const navigate = useNavigate();
  const backendUrl = "http://localhost:8080";

  
  const addToCart = (productId, size = "M", quantity = 1) => {
    const productExists = products.find((p) => p._id === productId);
    if (!productExists || quantity <= 0) return;

    setCartItems((prev) => {
      const updatedCart = { ...prev };
      updatedCart[productId] = updatedCart[productId] || {};
      updatedCart[productId][size] = (updatedCart[productId][size] || 0) + quantity;
      return updatedCart;
    });
  };

  
  const updateQuantity = (productId, size, quantity) => {
    setCartItems((prev) => {
      const updatedCart = { ...prev };
      if (!updatedCart[productId]) return updatedCart;

      if (quantity <= 0) {
        delete updatedCart[productId][size];
        if (Object.keys(updatedCart[productId]).length === 0) delete updatedCart[productId];
      } else {
        updatedCart[productId][size] = quantity;
      }
      return updatedCart;
    });
  };

  const getCartAmount = () => {
    let total = 0;
    for (const productId in cartItems) {
      const product = products.find((p) => p._id === productId);
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
        navigate,
        addToCart,
        updateQuantity,
        getCartAmount,
        getCartCount,
        currency,
        delivery_fee,
        backendUrl,
        token,       
        setToken,    
      }}
    >
      {children}
    </shopContext.Provider>
  );
};
