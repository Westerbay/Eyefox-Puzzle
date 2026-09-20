import { createRoot, type Root } from "react-dom/client"
import { EyefoxPuzzle } from "./react/EyefoxPuzzle"
import css from "./styles.css?inline"

export function defineEyefoxElement(tagName = "eyefox-puzzle") {
  if (typeof window === "undefined" || customElements.get(tagName)) return
  class EyefoxElement extends HTMLElement {
    static observedAttributes = ["locale", "theme", "level"]
    private root?: Root
    private container?: HTMLDivElement
    private revision = 0
    connectedCallback() {
      if (!this.shadowRoot) {
        const shadow = this.attachShadow({ mode: "open" })
        const style = document.createElement("style")
        style.textContent = ":host{display:block;min-width:0}" + css
        this.container = document.createElement("div")
        shadow.append(style, this.container)
      }
      if (!this.root) this.root = createRoot(this.container!)
      this.render()
    }
    disconnectedCallback() {
      this.root?.unmount()
      this.root = undefined
    }
    attributeChangedCallback(
      name: string,
      previous: string | null,
      next: string | null,
    ) {
      if (name === "level" && previous !== next) this.revision++
      this.render()
    }
    private render() {
      if (!this.root) return
      const theme = this.getAttribute("theme")
      const rawLevel = Number(this.getAttribute("level") ?? 0)
      const level =
        Number.isSafeInteger(rawLevel) && rawLevel >= 0 ? rawLevel : 0
      this.root.render(
        <EyefoxPuzzle
          key={this.revision}
          initialLevel={level}
          locale={this.getAttribute("locale") === "fr" ? "fr" : "en"}
          theme={theme === "dark" || theme === "light" ? theme : "system"}
          onComplete={(result) =>
            this.dispatchEvent(
              new CustomEvent("puzzlecomplete", {
                detail: result,
                bubbles: true,
                composed: true,
              }),
            )
          }
        />,
      )
    }
  }
  customElements.define(tagName, EyefoxElement)
}
