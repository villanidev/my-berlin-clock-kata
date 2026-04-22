import { useConverter } from "../hooks/useConverter";
import ToBerlinForm from "../components/ToBerlinForm";
import ToDigitalForm from "../components/ToDigitalForm";

export default function ConverterPage() {
  const vm = useConverter();

  return (
    <div>
      <h1>Berlin Clock Converter</h1>

      <div className="converter-grid">
        <ToBerlinForm
          digitalTime={vm.digitalTime}
          onDigitalTimeChange={vm.setDigitalTime}
          onSubmit={vm.convertToBerlin}
          error={vm.berlinError}
          result={vm.clockResult}
        />

        <ToDigitalForm
          berlinInput={vm.berlinInput}
          onBerlinInputChange={vm.setBerlinInput}
          onSubmit={vm.convertToDigital}
          error={vm.digitalError}
          result={vm.timeResult}
        />
      </div>
    </div>
  );
}
