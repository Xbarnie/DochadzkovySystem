<template>
  <div>
    <v-row>
      <v-col>
        <v-data-table v-if="history.length > 0"
          :headers="headers"
          :items="history"
          :options="paginationOptions" 
          :server-items-length="totalNumberOfItems"
          @pagination="paginationTable"
          class="elevation-1"
        >
          <template v-slot:[`item.editTime`]="{ item }">
              <span v-if="item.editTime != null">{{
                item.editTime | moment("DD.MM.YYYY HH:mm")
              }}</span>
          </template>
          <template v-slot:[`item.updated`]="{ item }">
              <span v-if="item.updated != null">{{
                $t(item.updated) 
              }}</span>
          </template>
        </v-data-table>
      </v-col>
    </v-row>
  </div>
</template>

<script>
  export default {
    name: 'Editing-History',
    data() {
      return {
        paginationOptions:
        {
          page: 1,
          itemsPerPage: 20,
        },
        totalNumberOfItems: 0,
        history: []
      }
    },
    methods: {
      getHistory(page, size) {
        this.$axios
        .get(`/api/attendance/history?page=${page}&size=${size}`)
        .then((response) => {
          console.log(response);
          
          console.log("total", response.data.totalElements)
          this.totalNumberOfItems = response.data.totalElements;
          this.history = response.data.content;
        })
        .catch((error) => {
          console.log(error);
        });
      },
      paginationTable(pagination) {
        console.log("pagination", pagination)
        var page = pagination.page -1
        this.$axios
        .get(`/api/attendance/history?page=${page}&size=${pagination.itemsPerPage}`)
        .then((response) => {
          console.log(response);
          
          console.log("total pagination", response.data.totalElements)
          this.history = response.data.content;
        })
        .catch((error) => {
          console.log(error);
        });
      }
    },
    mounted() {
    this.$store.dispatch('setToolbar', "editingHistory");
    this.getHistory(0, 20);
  },
    computed: {
      headers() {
        return [
          { text: this.$t('editTime'), value: 'editTime', },
          { text: this.$t('edited'), value: 'updated' },
          { text: this.$t('oldValue'), value: 'oldValue' },
          { text: this.$t('newValue'), value: 'newValue' },
          { text: this.$t('attendanceId'), value: 'attendance.id' },
        ]
      }
    },
  }
</script>

<style scoped>

</style>