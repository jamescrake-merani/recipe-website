(ns recipe-website.views
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
        [:nav.navbar.navbar-expand.bg-body-tertiary
         [:div.container-fluid
          [:a.navbar-brand {:href "/"} "Recipe Website"]
          [:button.navbar-toggler {:type "button"
                                   :data-bs-toggle "collapse"
                                   :data-bs-target "navbarSupportedContent"
                                   :aria-controls "navbarSupportedContent"
                                   :aria-expanded "false"
                                   :aria-label "Toggle navigation"}
           [:span.navbar-toggler-icon]]
          [:div#navbarSupportedContent.collapse.navbar-collapse
           [:ul.navbar-nav.me-auto.mb-2.mb-lg-0
            [:li.nav-item
             [:a.nav-link {:href "#"} "Recipes"]]]]]]
        [:div.container
         current-page]]]))

;; TODO: This is all placeholder stuff.

(defn recipe-page [recipe]
  (template
   (h/html
       [:div
        [:h1 (:recipe/name recipe)]
        ;; TODO: Format time guidance properly
        [:i.fw-light "Takes ~" (str (:recipe/time_guidance recipe)) " minutes"]
        [:h2 "Ingredients"]
        [:ul
         (doall (map (fn [ingredient]
                       [:li
                        (str (:ingredient/quantity ingredient))
                        " "
                        (:ingredient/measurement ingredient)
                        " of "
                        (:ingredient/ingredient_name ingredient)])
                     (:ingredients recipe)))]
        [:hr]
        [:h2 "Steps"]
        [:ol
         (doall (map (fn [line]
                       [:li (:recipe_line/content line)])
                     (:lines recipe)))]])))

(defn home-page []
  (template
   (h/html
       [:div
        [:h1 "This is the home page!"]
        [:p "It doesn't have a lot happening at the moment."]])))
