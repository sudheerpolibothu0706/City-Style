import { Route, Routes } from "react-router-dom";
import Home from "./pages/Home";
import Contactus from "./pages/Contactus";
import Collection from "./pages/Collection";
import Payment from "./pages/Payment";
import Myorders from "./pages/Myorders";
import Login from "./pages/Login";
import Aboutus from "./pages/Aboutus";
import Product from "./pages/Product";

import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import Cart from "./pages/Cart";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import PlaceOrders from "./pages/PlaceOrders";

function App() {
  return (
    <div>
      <Navbar />

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/contactus" element={<Contactus />} />
        <Route path="/collection" element={<Collection />} />
        <Route path="/cart" element={<Cart />} />
        <Route path="/login" element={<Login />} />
        <Route path="/payment" element={<Payment />} />
        <Route path="/myorders" element={<Myorders />} />
        <Route path="/product/:productId" element={<Product />} />
        <Route path="/aboutus" element={<Aboutus />} />
        
        <Route path="/place-order" element={<PlaceOrders/>}/>
        
        <Route path="/success" element={<h1>Payment Successful 🎉</h1>} />
        <Route path="/cancel" element={<h1>Payment Cancelled ❌</h1>} />

      </Routes>

      <Footer />

      
      <ToastContainer position="top-right" autoClose={3000} />
    </div>
  );
}

export default App;
