(ns recipe-website.controller
  (:require [reitit.ring :as ring]
            [recipe-website.views :as views]
            [ring.util.http-response :as response]
            [recipe-website.model :as model]
            [ring.middleware.defaults :as defaults]
            [reitit.coercion.schema]
            [schema.core :as s]
            [reitit.ring.coercion :as c]))

(defn html-response [content]
  (-> (str content)
      response/ok
      (response/content-type "text/html")))

(defn home-controller [req]
  (html-response (views/home-page)))

(defn all-recipe-controller [req]
  (html-response
   (views/all-recipe-page (model/get-all-recipes (:db req)))))

(defn recipe-controller [req]
  (let [recipe-id (get-in req [:parameters :path :id])
        recipe (model/get-recipe (:db req) recipe-id)
        recipe-lines (model/get-recipe-lines (:db req) recipe-id)
        recipe-ingredients (model/get-recipe-ingredents (:db req) recipe-id)]
    (html-response
   (views/recipe-page
    recipe
    recipe-ingredients
    recipe-lines))))

(def middleware-db
  {:name ::db
   :compile (fn [{:keys [db]} _]
              (fn [handler]
                (fn [req]
                  (handler (assoc req :db db)))))})

(defn creater-handler [db]
  (ring/ring-handler
   (ring/router
    [["/" {:get home-controller}]
     ["/recipe/:id" {:get
                     {:coercion reitit.coercion.schema/coercion
                      :handler recipe-controller
                      :parameters {:path {:id s/Int}}
                      }}]
     ["/all-recipes" {:get all-recipe-controller}]]

    {:data {:db db
            :middleware [middleware-db
                         c/coerce-exceptions-middleware
                         c/coerce-request-middleware
                         c/coerce-response-middleware]}})))
