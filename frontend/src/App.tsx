import { BrowserRouter, Routes, Route, NavLink } from "react-router-dom";
import { ConverterView } from "./features/converter";
import { LiveClockView } from "./features/berlin-clock";

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
          <Route path="/" element={<ConverterView />} />
          <Route path="/live" element={<LiveClockView />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}
