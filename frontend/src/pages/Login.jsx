import React, { useContext, useEffect, useState } from "react";
import { shopContext } from "../context/ShopContext";
import axios from "axios";
import { toast } from "react-toastify";

function Login() {
  const [currentState, setCurrentState] = useState("Login");
  const { navigate, backendUrl, token, setToken } = useContext(shopContext);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [otp, setOtp] = useState(""); 
  const [showOtpBox, setShowOtpBox] = useState(false);

  const [forgotEmail, setForgotEmail] = useState(""); 
  const [resetOtp, setResetOtp] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [showResetBox, setShowResetBox] = useState(false);

  const [resendTimer, setResendTimer] = useState(0); 

  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  const passwordRegex =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

  useEffect(() => {
    if (token) navigate("/");
  }, [token]);

  useEffect(() => {
    if (resendTimer > 0) {
      const interval = setInterval(() => setResendTimer((prev) => prev - 1), 1000);
      return () => clearInterval(interval);
    }
  }, [resendTimer]);

  const validateEmail = (email) => emailRegex.test(email);
  const validatePassword = (password) => passwordRegex.test(password);
  const handleSignup = async (e) => {
    e.preventDefault();
    if (!validateEmail(email)) return toast.error("Invalid email");
    if (!validatePassword(password))
      return toast.error(
        "Password must be 8+ chars, include uppercase, lowercase, number & special char"
      );
    if (password !== confirmPassword) return toast.error("Passwords do not match");

    try {
      const res = await axios.post(`${backendUrl}/api/v1/user/registration`, {
        name,
        email,
        password,
      });
      toast.success(res.data.message);
      setShowOtpBox(true);
    } catch (err) {
      toast.error(err.response?.data?.message || "Signup failed");
    }
  };

  const handleVerifyOtp = async () => {
    if (!otp) return toast.error("Enter OTP");
    try {
      const res = await axios.post(
        `${backendUrl}/api/v1/user/verify-email`,
        null,
        { params: { email, otp } }
      );
      toast.success(res.data);
      setShowOtpBox(false);
      setCurrentState("Login");
    } catch (err) {
      toast.error(err.response?.data || "Invalid or expired OTP");
    }
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    if (!validateEmail(email)) return toast.error("Invalid email");
    if (!password) return toast.error("Enter password");

    try {
      const res = await axios.post(`${backendUrl}/api/v1/user/login`, { email, password });
      if (res.data.token) {
        setToken(res.data.token);
        localStorage.setItem("token", res.data.token);
        toast.success(res.data.message);
        navigate("/");
      } else toast.error(res.data.message || "Login failed");
    } catch (err) {
      toast.error(err.response?.data?.message || "Login failed");
    }
  };

  const handleForgotPassword = async () => {
    if (!validateEmail(forgotEmail)) return toast.error("Enter a valid email");
    try {
      const res = await axios.post(`${backendUrl}/api/v1/user/forgot-password`, {
        email: forgotEmail,
      });
      toast.success(res.data);
      setShowResetBox(true);
      setResendTimer(60);
    } catch (err) {
      toast.error(err.response?.data || "Error sending OTP");
    }
  };

  const handleResetPassword = async () => {
    if (!validatePassword(newPassword))
      return toast.error(
        "Password must be 8+ chars, include uppercase, lowercase, number & special char"
      );
    if (!resetOtp) return toast.error("Enter OTP");

    try {
      const res = await axios.post(`${backendUrl}/api/v1/user/reset-password`, {
        email: forgotEmail,
        otp: resetOtp,
        newPassword,
      });
      toast.success(res.data);
      setShowResetBox(false);
      setCurrentState("Login");
    } catch (err) {
      toast.error(err.response?.data || "Invalid OTP or error resetting password");
    }
  };

  const handleResendOtp = async (emailToSend) => {
    if (!emailToSend) return;
    try {
      await axios.post(`${backendUrl}/api/v1/user/forgot-password`, { email: emailToSend });
      toast.success("OTP resent to your email");
      setResendTimer(60);
    } catch (err) {
      toast.error(err.response?.data || "Error resending OTP");
    }
  };

  return (
    <div className="flex flex-col items-center justify-center mt-10 text-gray-800">
      <div className="inline-flex items-center gap-2 mb-2 mt-10">
        <p className="text-3xl font-semibold">{currentState}</p>
        <hr className="border-none h-[1.5px] w-8 bg-gray-800" />
      </div>

      {currentState === "Login" && (
        <form
          onSubmit={handleLogin}
          className="flex flex-col gap-4 w-[90%] sm:max-w-96"
        >
          <input
            type="email"
            placeholder="Email"
            className="w-full px-3 py-2 border"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Password"
            className="w-full px-3 py-2 border"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <div className="flex justify-between text-sm mt-[-8px]">
            <p className="cursor-pointer text-blue-500" onClick={() => setCurrentState("Recover Password")}>
              Forgot your password?
            </p>
            <p className="cursor-pointer text-blue-500" onClick={() => setCurrentState("SignUp")}>
              Create account
            </p>
          </div>
          <button className="bg-black text-white font-light px-8 py-2 mt-4">Login</button>
        </form>
      )}

      {currentState === "SignUp" && (
        <form
          onSubmit={handleSignup}
          className="flex flex-col gap-4 w-[90%] sm:max-w-96"
        >
          <input
            type="text"
            placeholder="Name"
            className="w-full px-3 py-2 border"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
          <input
            type="email"
            placeholder="Email"
            className="w-full px-3 py-2 border"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Password"
            className="w-full px-3 py-2 border"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Confirm Password"
            className="w-full px-3 py-2 border"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            required
          />
          <p className="text-xs text-gray-600">
            ⚠️ Password must be at least 8 characters and include uppercase,
            lowercase, number, and special character.
          </p>
          <button className="bg-black text-white font-light px-8 py-2 mt-4">Sign Up</button>
          <p
            onClick={() => setCurrentState("Login")}
            className="cursor-pointer text-blue-500 text-center"
          >
            Already have an account? Login
          </p>
        </form>
      )}

      {showOtpBox && (
        <div className="flex flex-col gap-4 mt-6 w-[90%] sm:max-w-96">
          <p className="text-sm text-gray-700">
            Enter the OTP sent to your email for verification
          </p>
          <input
            type="text"
            placeholder="Enter OTP"
            className="w-full px-3 py-2 border"
            value={otp}
            onChange={(e) => setOtp(e.target.value)}
          />
          <div className="flex gap-2">
            <button
              onClick={handleVerifyOtp}
              className="bg-green-600 text-white px-6 py-2 rounded"
            >
              Verify Email
            </button>
            <button
              onClick={() => handleResendOtp(email)}
              disabled={resendTimer > 0}
              className={`px-6 py-2 rounded border ${
                resendTimer > 0 ? "text-gray-500 border-gray-300" : "text-blue-500 border-blue-500"
              }`}
            >
              {resendTimer > 0 ? `Resend OTP (${resendTimer})` : "Resend OTP"}
            </button>
          </div>
        </div>
      )}

      {currentState === "Recover Password" && !showResetBox && (
        <div className="flex flex-col gap-4 w-[90%] sm:max-w-96">
          <p className="text-sm text-gray-700">
            Enter your email to receive OTP for password reset
          </p>
          <input
            type="email"
            placeholder="Your Email"
            className="w-full px-3 py-2 border"
            value={forgotEmail}
            onChange={(e) => setForgotEmail(e.target.value)}
          />
          <button
            onClick={handleForgotPassword}
            className="bg-black text-white px-6 py-2"
          >
            Send OTP
          </button>
          <p
            onClick={() => setCurrentState("Login")}
            className="cursor-pointer text-blue-500 text-center"
          >
            Back to Login
          </p>
        </div>
      )}
      {showResetBox && (
        <div className="flex flex-col gap-4 mt-6 w-[90%] sm:max-w-96">
          <p className="text-sm text-gray-700">
            Enter the OTP sent to your email and your new password
          </p>
          <input
            type="text"
            placeholder="Enter OTP"
            className="w-full px-3 py-2 border"
            value={resetOtp}
            onChange={(e) => setResetOtp(e.target.value)}
          />
          <input
            type="password"
            placeholder="New Password"
            className="w-full px-3 py-2 border"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
          />
          <div className="flex gap-2">
            <button
              onClick={handleResetPassword}
              className="bg-green-600 text-white px-6 py-2 rounded"
            >
              Reset Password
            </button>
            <button
              onClick={() => handleResendOtp(forgotEmail)}
              disabled={resendTimer > 0}
              className={`px-6 py-2 rounded border ${
                resendTimer > 0 ? "text-gray-500 border-gray-300" : "text-blue-500 border-blue-500"
              }`}
            >
              {resendTimer > 0 ? `Resend OTP (${resendTimer})` : "Resend OTP"}
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
export default Login;
