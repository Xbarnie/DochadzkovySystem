<template>
    <v-content>
            <v-container fluid>
                <v-row>
                    <v-col v-for="(item,i) in 5" :key="i" class="pa-0">
                        <v-expansion-panels accordion v-model="panel" multiple>
                        <v-expansion-panel
                            v-for="(item,i) in 5"
                            :key="i"
                        >
                            <v-expansion-panel-header>Item</v-expansion-panel-header>
                            <v-expansion-panel-content>
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit, 
                            </v-expansion-panel-content>
                        </v-expansion-panel>
                        </v-expansion-panels>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col>
                        <v-expansion-panels accordion multiple>
                        <v-expansion-panel
                            v-for="(item,i) in 5" :key="i"
                        >
                            <v-expansion-panel-header>
                                <v-row>
                                    <v-col v-for="(item,i) in 6" :key="i" class="pa-0">
                                    </v-col>
                                </v-row>
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                                <v-row>
                                    <v-col v-for="(item,i) in 6" :key="i" class="pa-0">
                                        <v-card> ahoj</v-card>
                                    </v-col>
                                </v-row>
                            </v-expansion-panel-content>
                        </v-expansion-panel>
                        </v-expansion-panels>
                    </v-col>
                </v-row>
                <v-row>
                    <v-col>
                        <v-row v-for="(item,i) in 15" :key="i">
                            <v-col md="12" @click="showHidden(i)">
                                <v-row>
                                    <v-col v-for="(item,i) in 6" :key="i" class="pa-0">
                                        <span>show</span>
                                    </v-col>
                                </v-row>
                            </v-col>
                            <v-col md="12" v-if="display.includes(i)">
                                <v-row>
                                    <v-col v-for="(item,i) in 6" :key="i" class="pa-0"> 
                                        <span>hide</span>
                                    </v-col>
                                </v-row>
                            </v-col>
                        </v-row>
                    </v-col>
                </v-row>
            </v-container>
        </v-content>
</template>

<script>

export default {
    name: 'Inteway',
    data () {
      return {
          panel: [1],
          isShow: false,
          display: [],
      }
    },
    methods: {
        showHidden(index) {
            console.log(index);
            if(this.display.includes(index)) {
                var idx = this.display.indexOf(index);
                //this.display.remove(index);
                this.display.splice(idx, 1);
            } else {
                this.display.push(index);
            }
        }
    }
}
</script>