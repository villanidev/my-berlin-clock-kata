import { BrowserRouter, Routes, Route, NavLink } from "react-router-dom";
import { ConverterPage } from "./features/converter";
import { LiveClockPage } from "./features/berlin-clock";

export default function App() {
  return (
    <BrowserRouter>
      <div className="app">
        <nav>
          <NavLink to="/" end>
            Converter
          </NavLink>
          <NavLink to="/live">Live Clock</NavLink>
        </nav>
        <Routes>
          <Route path="/" element={<ConverterPage />} />
          <Route path="/live" element={<LiveClockPage />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}
