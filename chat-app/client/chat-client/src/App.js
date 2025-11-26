import "./App.css";
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Register from './components/Register';
import MainPage from './components/MainPage';
import ChatRoom from './components/ChatRoom';

export default function App() {

  return (
    <>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/register" element={<Register />} />
        <Route path="/chatRoom" element={<ChatRoom />} />
      </Routes>
    </BrowserRouter>
    </>
  );
}