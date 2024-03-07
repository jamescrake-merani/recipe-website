(ns recipe-website.model
  (:require [honey.sql :as sql]))

(def recipe-table
  (sql/format
   {:create-table "recipe"
    :with-columns [[:id :primary-key :auto-increment]
                   [:name :text]
                   [:time-guidance :int]
                   [:published :boolean]]}))
