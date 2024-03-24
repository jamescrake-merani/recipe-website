(ns recipe-website.core
  (:require [integrant.core :as ig]
            [next.jdbc :as jdbc]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :as defaults]))

(defn handler [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "<h1>Test Output</h1>"})

(def config
  {::http-server {:port 8080
                  :db (ig/ref ::db-connection)}
   ::db-connection {:db-file "data.db"}})

(defmethod ig/init-key ::db-connection [_ {:keys [db-file]}]
  (jdbc/get-connection {:dbtype "sqlite" :dbname db-file}))

(defmethod ig/init-key ::http-server [_ {:keys [port]}]
  (jetty/run-jetty (defaults/wrap-defaults #'handler defaults/site-defaults)
                   {:join? false
                    :port port}))

(defmethod ig/halt-key! ::http-server [_ server]
  (.stop server))

(comment
  (def system
    (ig/init config))
  (ig/halt! system))
