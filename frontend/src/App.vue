<template>
  <div class="App-dev">
    <AlertPopup v-if="modalIsOpen"></AlertPopup>
    <router-view></router-view>
  </div>
</template>

<script>
import AlertPopup from "@/views/common/AlertPopup.vue";
import {mapState} from "vuex";

export default {
  components: {
    AlertPopup
  },
  computed: {
    ...mapState({
      modalIsOpen: state => state.openModal.isOpen,
    }),
  },
}
</script>

<style>
@import "@/assets/css/reset.css";
@import "@/assets/css/common.css";
@import "@/assets/css/style.css";
</style>