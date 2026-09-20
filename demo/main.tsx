import React from "react"
import { createRoot } from "react-dom/client"
import { EyefoxPuzzle } from "../src"
import { defineEyefoxElement } from "../src/element"
import "../src/styles.css"
import "./styles.css"

defineEyefoxElement()
function Demo() {
  const [locale, setLocale] = React.useState<"fr" | "en">("en")
  const [theme, setTheme] = React.useState<"light" | "dark">(() =>
    matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light",
  )
  React.useEffect(() => {
    document.documentElement.lang = locale
    document.documentElement.dataset.theme = theme
  }, [locale, theme])
  return (
    <main>
      <div className="demo-controls">
        <button
          onClick={() => setLocale(locale === "fr" ? "en" : "fr")}
          aria-label={
            locale === "fr" ? "Switch to English" : "Passer en français"
          }
        >
          FR / EN
        </button>
        <button onClick={() => setTheme(theme === "dark" ? "light" : "dark")}>
          {theme === "dark"
            ? locale === "fr"
              ? "Thème clair"
              : "Light theme"
            : locale === "fr"
              ? "Thème sombre"
              : "Dark theme"}
        </button>
      </div>
      <EyefoxPuzzle locale={locale} theme={theme} />
      <nav
        className="demo-links"
        aria-label={locale === "fr" ? "Liens du projet" : "Project links"}
      >
        <a href="https://github.com/Westerbay/Eyefox-Puzzle/tree/web">
          {locale === "fr" ? "Code et crédits" : "Source and credits"}
        </a>
        <a href="./privacy.html">
          {locale === "fr"
            ? "Confidentialité du jeu Android"
            : "Android privacy policy"}
        </a>
      </nav>
    </main>
  )
}
createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <Demo />
  </React.StrictMode>,
)
