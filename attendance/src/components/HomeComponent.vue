<template>
    <v-container>
        <v-row>
            <v-col>
                <v-card @click="employeePage($t('homePage.lunch'))" height="100" class="center-vh mb-2" >
                    <span>{{$t('employee')}}</span>
                </v-card>
            </v-col>
            <v-col>
                <v-card @click="administratorPage($t('homePage.lunch'))" height="100" class="center-vh mb-2">
                    <span>{{$t('administrator')}}</span>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>


<script>

export default {
    name: 'HomeComponent',
    data() {
        return {
        }
    },
    methods: {
        employeePage() {

        },
        administratorPage() {

        }
    },
    mounted() {
        console.log("Set admin false")
        this.$store.dispatch('setAdministration', false);
    },
    created() {
        console.log("Set admin false")
        //this.$store.dispatch('setAdministration', 'true');
  }
}
</script>