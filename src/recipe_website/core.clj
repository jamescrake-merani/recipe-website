(ns recipe-website.core
  (:require [integrant.core :as ig]
            [next.jdbc :as jdbc]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :as defaults]
            [recipe-website.controller :as controller]))

(def config
  {::http-server {:port 8080
                  :db (ig/ref ::db-connection)}
   ::db-connection {:db-file "data.db"}})

(defmethod ig/init-key ::db-connection [_ {:keys [db-file]}]
  (jdbc/get-connection {:dbtype "sqlite" :dbname db-file}))

(defmethod ig/init-key ::http-server [_ {:keys [port db]}]
  (jetty/run-jetty (defaults/wrap-defaults (controller/creater-handler db) defaults/site-defaults)
                   {:join? false
                    :port port}))

(defmethod ig/halt-key! ::http-server [_ server]
  (.stop server))

(comment
  (def system
    (ig/init config))
  (ig/halt! system))
