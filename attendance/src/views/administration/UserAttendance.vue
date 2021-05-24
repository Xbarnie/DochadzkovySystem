<template>
    <div v-if="user != null">
        <v-row>
            <v-col>
                <h2>{{user.firstName}} {{user.lastName}}</h2> 
                <span>({{user.department.name}})</span>
            </v-col>
        </v-row>
        <v-row>
            <v-col>
                <v-data-table
                    :headers="headers"
                    :items="tableAttendance"
                    :items-per-page="20"
                    class="elevation-1"
                    >
                    <template v-slot:[`item.user`]="{ item }">
                        <span>{{ item.user.firstName }} {{ item.user.lastName }}</span>
                    </template>
                    <template v-slot:[`item.department`]="{ item }">
                        <span>{{ item.user.department.name }}</span>
                    </template>
                    <template v-slot:[`item.arrivalAt`]="{ item }">
                        <span v-if="item.arrivalAt != null">{{
                        item.arrivalAt | moment("DD.MM.YYYY HH:MM:ss")
                        }}</span>
                    </template>
                    <template v-slot:[`item.leaveAt`]="{ item }">
                        <span v-if="item.leaveAt != null">{{
                        item.leaveAt | moment("DD.MM.YYYY HH:MM:ss")
                        }}</span>
                    </template>
                    <template v-slot:[`item.lunchAt`]="{ item }">
                        <span v-if="item.lunchAt != null">{{
                        item.lunchAt | moment("DD.MM.YYYY HH:MM:ss")
                        }}</span>
                    </template>
                    </v-data-table>
            </v-col>
        </v-row>
    </div>
</template>

<script>
export default {
    name: 'UserAttendance',
    data() {
        return {
            user: null,
            allAttendance: [],
            tableAttendance: [],
            headers: [
                { text: this.$t("arrival"), value: "arrivalAt" },
                { text: this.$t("leaving"), value: "leaveAt" },
                { text: this.$t("lunch"), value: "lunchAt" },
            ],
        }
    },
    mounted() {
        this.getUser(this.$route.params.id)
    },
    methods: {
        getUser(id) {
            console.log(id);
            this.$axios.get("/api/user/"+id).then((response) => {
                if(response.data != "") {
                    console.log(response.data);
                    this.user = response.data.user;
                    this.allAttendance = response.data.attendance;
                    this.tableAttendance = response.data.attendance;
                    this.$store.dispatch('setToolbar', this.user.firstName + " "+ this.user.lastName);
                } else {
                    console.log("User doesnt exist");
                }
            })
            .catch((error) => {
            console.log(error);
            });
        }
    }
}
</script>