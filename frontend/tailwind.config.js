/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        canvas: "#F9FAFB",
        surface: "#FFFFFF",
        border: "#E5E7EB",
        ink: {
          DEFAULT: "#111827",
          muted: "#6B7280",
          faint: "#9CA3AF",
        },
        primary: {
          DEFAULT: "#4338CA",
          light: "#EEF2FF",
          hover: "#3730A3",
        },
        success: {
          DEFAULT: "#059669",
          light: "#ECFDF5",
        },
        warn: {
          DEFAULT: "#B45309",
          light: "#FFFBEB",
        },
        danger: {
          DEFAULT: "#DC2626",
          light: "#FEF2F2",
        },
      },
      fontFamily: {
        sans: ["Inter", "system-ui", "sans-serif"],
        mono: ["IBM Plex Mono", "monospace"],
      },
      boxShadow: {
        card: "0 1px 2px 0 rgb(0 0 0 / 0.04), 0 1px 3px 0 rgb(0 0 0 / 0.06)",
      },
    },
  },
  plugins: [],
};
