(ns status-im.contexts.onboarding.intro.view
  (:require
    [quo.core :as quo]
    [react-native.core :as rn]
    [status-im.constants :as constants]
    [status-im.contexts.onboarding.common.background.view :as background]
    [status-im.contexts.onboarding.common.overlay.view :as overlay]
    [status-im.contexts.onboarding.intro.style :as style]
    [status-im.contexts.syncing.scan-sync-code.view :as scan-sync-code]
    [utils.debounce :as debounce]
    [utils.i18n :as i18n]))

(defn view
  []
  [rn/view {:style style/page-container}
   [background/view false]
   [quo/drawer-buttons
    {:on-init             (fn [reset-top-animation-fn]
                            (reset! scan-sync-code/dismiss-animations reset-top-animation-fn))
     :animations-duration constants/onboarding-modal-animation-duration
     :animations-delay    constants/onboarding-modal-animation-delay
     :top-card            {:on-press            #(debounce/dispatch-and-chill [:open-modal
                                                                               :sign-in-intro]
                                                                              2000)
                           :heading             (i18n/label :t/sign-in)
                           :animated-heading    (i18n/label :t/sign-in-by-syncing)
                           :accessibility-label :already-use-status-button}
     :bottom-card         {:on-press            (fn []
                                                  (when-let [blur-show-fn @overlay/blur-show-fn-atom]
                                                    (blur-show-fn))
                                                  (debounce/dispatch-and-chill
                                                   [:open-modal :new-to-status]
                                                   1000))
                           :heading             (i18n/label :t/new-to-status)
                           :accessibility-label :new-to-status-button}}
    [quo/text
     {:size   :paragraph-2
      :weight :regular
      :style  style/plain-text}
     (i18n/label :t/you-already-use-status)]
    [quo/text {:style style/text-container}
     [quo/text
      {:size   :paragraph-2
       :weight :regular
       :style  style/plain-text}
      (i18n/label :t/by-continuing-you-accept)]
     [quo/text
      {:on-press #(debounce/dispatch-and-chill [:open-modal :privacy-policy] 1000)
       :size     :paragraph-2
       :weight   :regular
       :style    style/highlighted-text}
      (i18n/label :t/terms-of-service)]]]
   [overlay/view]])
