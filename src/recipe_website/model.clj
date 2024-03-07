(ns recipe-website.model
  (:require [honey.sql :as sql]
            [next.jdbc :as jdbc]))

(def recipe-table
  (sql/format
   {:create-table :recipe
    :with-columns [[:id :primary-key]
                   [:name :text]
                   [:time-guidance :int]
                   [:published :boolean]]}))

(def tag
  (sql/format
   {:create-table :tag
    :with-columns [[:recipe-id :int ]
                   [:name :text]
                   [[:primary-key [:recipe-id :name]]
                    [:foreign-key [:references :recipe :id]]]]}))

(def recipe-line
  (sql/format
   {:create-table :recipe-line
    :with-columns [[:recipe-id :int ]
                   [:order :int]
                   [:content :text]
                   [[:primary-key [:recipe-id :order]]
                    [:foreign-key [:references :recipe :id]]]]}))

(defn create-tables [db]
  (jdbc/execute-one! db recipe-table)
  (jdbc/execute-one! db tag)
  (jdbc/execute-one! db recipe-line))
