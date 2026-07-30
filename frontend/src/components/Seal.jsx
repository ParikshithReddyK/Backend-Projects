export default function Seal({ size = 36 }) {
  return (
    <div
      className="rounded-lg bg-primary flex items-center justify-center text-white font-bold shrink-0"
      style={{ width: size, height: size, fontSize: size * 0.4 }}
    >
      C
    </div>
  );
}
