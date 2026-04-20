interface LampProps {
  state: string;
  size: "wide" | "narrow" | "seconds";
  title?: string;
}

export default function Lamp({ state, size, title }: LampProps) {
  return <div className={`lamp lamp--${size} ${state}`} title={title} />;
}
