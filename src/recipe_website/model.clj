(ns recipe-website.model
  (:require [honey.sql :as sql]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as jsql]))

(def recipe-table
  (sql/format
   {:create-table [:recipe :if-not-exists]
    :with-columns [[:id :primary-key]
                   [:name :text]
                   [:time-guidance :int]
                   [:published :boolean]]}))

(def tag
  (sql/format
   {:create-table [:tag :if-not-exists]
    :with-columns [[:recipe-id :int :references [:recipe :id]]
                   [:name :text]
                   [[:primary-key :recipe-id :name]]]}))

(def recipe-line
  (sql/format
   {:create-table [:recipe-line :if-not-exists]
    :with-columns [[:recipe-id :int :references [:recipe :id]]
                   [:line-order :int]
                   [:content :text]
                   [[:primary-key :recipe-id :line-order]]]}))

;; TODO: Need to retrive recipe lines as well.
(defn get-recipe [db recipe-id]
  (jdbc/execute-one!
   db
   (sql/format
    {:select :* :from :recipe :where [:= :id recipe-id]})))

(defn insert-recipe [recipe db]
  "RECIPE is represented as a map with the following keys
:name
:time-guidance
:published
:lines (vector of lines assumed to be in order) as follows:

:conten
"
  (let [recipe-id
        (-> (jdbc/execute-one!
             db
             (sql/format
              {:insert-into :recipe
               :columns [:name :time-guidance :published]
               :values [[(:name recipe) (:time-guidance recipe) (:published recipe)]]})
             {:return-keys true})
            (vals)
            (first))
        lines (:lines recipe)]
    (jdbc/execute-one!
     db
     (sql/format
      {:insert-into :recipe-line
       :columns [:recipe-id :line-order :content]
       :values (map (fn [line order]
                      [recipe-id order line])
                    lines (range (count lines)))}))))

(defn create-tables [db]
  (doseq [table [recipe-table tag recipe-line]]
    (jdbc/execute-one! db table)))
