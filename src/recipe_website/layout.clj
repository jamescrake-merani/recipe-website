(ns recipe-website.layout
  (:require [hiccup2.core :as h]))

(defn template [current-page]
  (h/html
      [:html {:lang "en"}
       [:head
        [:link {:rel "stylesheet"
                :href "https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
                :integrity "sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN"
                :crossorigin "anonymous"}]]
       [:body
        [:h1 "Test Template"]
        [:div.container
         current-page]]]))
