(ns recipe-website.core
  (:require [integrant.core :as ig]
            [next.jdbc :as jdbc]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :as defaults]
            [recipe-website.controller :as controller]))

(def config
  {::http-server {:port 8080
                  :handler {ig/ref ::app-handler}}
   ::db-connection {:db-file "data.db"}
   ::app-handler {:db (ig/ref ::db-connection)}})

(defmethod ig/init-key ::db-connection [_ {:keys [db-file]}]
  (jdbc/get-connection {:dbtype "sqlite" :dbname db-file}))

(defmethod ig/init-key ::app-handler [_ {:keys [db]}]
  (controller/creater-handler db))

(defmethod ig/init-key ::http-server [_ {:keys [port handler]}]
  (jetty/run-jetty (defaults/wrap-defaults handler defaults/site-defaults)
                   {:join? false
                    :port port}))

(defmethod ig/halt-key! ::http-server [_ server]
  (.stop server))

(comment
  (def system
    (ig/init config))
  (def db (::db-connection system))
  (ig/halt! system))
