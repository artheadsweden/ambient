(ns ambient.app
  (:require [reagent.core :as reagent :refer [atom]]))

(def url "https://arthead.io/")

(defn play [player icon slider]
  (set! (.-className slider) "vol-control-visible")
  (set! (.-className icon) "op_hundred")
  (.play player))

(defn pause [player icon slider]
  (set! (.-className slider) "vol-control-hidden")
  (set! (.-className icon) "op_twenty")
  (.pause player))

(defn set-volume [player value]
  (set! (.-volume player) (/ value 100)))

(defn audio-player [n]
  [:div {:key (str "player" n) :class "player"}
    [:audio {:id (str "player" n) :controls false :loop true}
        [:source {:src (str url "sfx/" n ".mp3")}]]
    [:img {:class "op_twenty" :name (str "player" n) :id (str "player" n "i") :src (str url "imgs/" n ".png")
            :style {:width "75px" :height "75px"}
            :on-click (fn [e]
                        (let [player (.getElementById js/document (str e.target.name))]
                          (let [icon (.getElementById js/document (str e.target.id))]
                            (let [slider (.getElementById js/document (str "player" n "v"))]
                              (cond
                                player.paused (play player icon slider)
                                :else (pause player icon slider))))))}]
    [:input {:class "vol-control-hidden" :id (str "player" n "v") :name (str "player" n) :type "range" :min "0" :max "100"
             :on-change (fn [e]
                          (let [player (.getElementById js/document (str e.target.name))]
                            (let [target (.-target e)]
                              (let [value (.-value target)]
                                (set-volume player value)))))}]])

(defn app-component []
  [:div {:class "container"}
    (map audio-player (range 1 27))])

(defn init []
  (reagent/render-component [app-component]
                            (.getElementById js/document "container")))
