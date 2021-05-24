<template>
  <div>
    <v-row>
      <v-col>
        <v-data-table
          :headers="attendanceHeader"
          :items="attendance"
          :items-per-page="20"
          class="elevation-1"
        >
          <template v-slot:[`item.user`]="{ item }">
            <a @click="userInfoModal(item.user.id)" class="text-decoration-none">{{ item.user.firstName }} {{ item.user.lastName }}</a>
          </template>
          <template v-slot:[`item.department`]="{ item }">
            <span>{{ item.user.department.name }}</span>
          </template>
          <template v-slot:[`item.arrivalAt`]="{ item }">
            <span v-if="item.arrivalAt != null">{{
              item.arrivalAt | moment("DD.MM.YYYY HH:mm")
            }}</span>
          </template>
          <template v-slot:[`item.leaveAt`]="{ item }">
            <span v-if="item.leaveAt != null">{{
              item.leaveAt | moment("DD.MM.YYYY HH:mm")
            }}</span>
          </template>
          <template v-slot:[`item.lunchFrom`]="{ item }">
            <span v-if="item.lunchFrom != null">{{ item.lunchFrom }} - </span><span v-if="item.lunchTo != null">{{ item.lunchTo }}</span>
          </template>
          <template v-slot:[`item.doctor`]="{ item }">
            <span v-if="item.doctorFrom != null">{{item.doctorFrom}} - </span><span v-if="item.doctorTo != null">{{item.doctorTo}}</span>
          </template>
          <template v-slot:[`item.businessFrom`]="{ item }">
            <span v-if="item.businessFrom != null">{{item.businessFrom}} - </span><span v-if="item.businessTo != null">{{item.businessTo}}</span>
          </template>
          <template v-slot:[`item.privateFrom`]="{ item }">
            <span v-if="item.privateFrom != null">{{item.privateFrom}} - </span><span v-if="item.privateTo != null">{{item.privateTo}}</span>
          </template>
          <template v-slot:[`item.vacationFrom`]="{ item }">
            <span v-if="item.vacationFrom != null">{{item.vacationFrom}} - </span><span v-if="item.vacationTo != null">{{item.vacationTo}}</span>
          </template>
          <template v-slot:[`item.actions`]="{ item }">
            <v-btn text icon color="blue" @click="editDialogOpen(item)"><v-icon>mdi-pencil</v-icon></v-btn>
            <v-btn text icon color="red" @click="deleteAttendance(item)"><v-icon>mdi-delete</v-icon></v-btn>
          </template>
        </v-data-table>
      </v-col>
    </v-row>
    <v-row justify="center">
      <v-dialog v-model="editDialog.isOpen" persistent width="800px">
      <v-card v-if="editDialog.user.user != null">
        <v-card-title>
          {{editDialog.user.user.firstName}} {{editDialog.user.user.lastName}} <br>
          <span class="subtitle-2 ml-2">({{editDialog.user.user.department.name}})</span>
        </v-card-title>
        <v-card-text>
        <v-container>
          <v-row>
            <v-col cols="12">
              <span class="display-1">{{$t('arrival')}}</span>
              <v-row>
                <v-col cols="6">
                  <v-dialog 
                    ref="arrivalModalDate"
                    v-model="editDialog.arrivalModalDate"
                    :return-value.sync="editDialog.user.arrivalAt[0]"
                    persistent
                    width="290px">
                        <template v-slot:activator="{ on, attrs }">
                          <v-text-field
                            v-model="editDialog.user.arrivalAt[0]"
                            :label="$t('arrivalDate')"
                            prepend-icon="mdi-calendar"
                            readonly
                            v-bind="attrs"
                            v-on="on"
                          ></v-text-field>
                        </template>
                        <v-date-picker
                          v-model="editDialog.user.arrivalAt[0]"
                          scrollable
                          locale="sk"
                        >
                          <v-spacer></v-spacer>
                          <v-btn
                            text
                            color="primary"
                            @click="editDialog.arrivalModalDate = false"
                          >
                            {{$t('homePage.cancel')}}
                          </v-btn>
                          <v-btn
                            text
                            color="primary"
                            @click="$refs.arrivalModalDate.save(editDialog.user.arrivalAt[0])"
                          >
                            OK
                          </v-btn>
                        </v-date-picker>
                    </v-dialog>
                </v-col>
                <v-col cols="6">
                  <v-dialog
                    ref="arrivalModalTime"
                    v-model="editDialog.arrivalModalTime"
                    :return-value.sync="editDialog.user.arrivalAt[1]"
                    persistent
                    width="290px">

                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="editDialog.user.arrivalAt[1]"
                          :label="$t('arrivalTime')"
                          prepend-icon="mdi-clock-time-four-outline"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                      </template>

                      <v-time-picker
                        v-if="editDialog.arrivalModalTime"
                        v-model="editDialog.user.arrivalAt[1]"
                        full-width
                        format="24hr"
                      >
                        <v-spacer></v-spacer>
                        <v-btn
                          text
                          color="primary"
                          @click="editDialog.arrivalModalTime = false"
                        >
                          Cancel
                        </v-btn>
                        <v-btn
                          text
                          color="primary"
                          @click="$refs.arrivalModalTime.save(editDialog.user.arrivalAt[1])"
                        >
                          OK
                        </v-btn>
                      </v-time-picker>

                  </v-dialog>
                </v-col>
              </v-row>
            </v-col>
            <v-col cols="12">
              <v-row>
                <v-col cols="12">
                  <span class="display-1">{{$t('leaving')}}</span>
                </v-col>
                <v-col cols="6">
                  <v-dialog 
                    ref="leaveModalDate"
                    v-model="editDialog.leaveModalDate"
                    :return-value.sync="editDialog.user.leaveAt[0]"
                    persistent
                    width="290px">
                        <template v-slot:activator="{ on, attrs }">
                          <v-text-field
                            v-model="editDialog.user.leaveAt[0]"
                            :label="$t('leaveDate')"
                            prepend-icon="mdi-calendar"
                            readonly
                            v-bind="attrs"
                            v-on="on"
                          ></v-text-field>
                        </template>
                        <v-date-picker
                          v-model="editDialog.user.leaveAt[0]"
                          scrollable
                          locale="sk"
                        >
                          <v-spacer></v-spacer>
                          <v-btn
                            text
                            color="primary"
                            @click="editDialog.leaveModalDate = false"
                          >
                            {{$t('homePage.cancel')}}
                          </v-btn>
                          <v-btn
                            text
                            color="primary"
                            @click="$refs.leaveModalDate.save(editDialog.user.leaveAt[0])"
                          >
                            OK
                          </v-btn>
                        </v-date-picker>
                    </v-dialog>
                </v-col>
                <v-col cols="6">
                  <v-dialog
                    ref="leaveModalTime"
                    v-model="editDialog.leaveModalTime"
                    :return-value.sync="editDialog.user.leaveAt[1]"
                    persistent
                    width="290px">

                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="editDialog.user.leaveAt[1]"
                          :label="$t('leaveTime')"
                          prepend-icon="mdi-clock-time-four-outline"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                      </template>

                      <v-time-picker
                        v-if="editDialog.leaveModalTime"
                        v-model="editDialog.user.leaveAt[1]"
                        full-width
                        format="24hr"
                      >
                        <v-spacer></v-spacer>
                        <v-btn
                          text
                          color="primary"
                          @click="editDialog.leaveModalTime = false"
                        >
                          Cancel
                        </v-btn>
                        <v-btn
                          text
                          color="primary"
                          @click="$refs.leaveModalTime.save(editDialog.user.leaveAt[1])"
                        >
                          OK
                        </v-btn>
                      </v-time-picker>

                  </v-dialog>
                </v-col>
              </v-row>
            </v-col>
            <v-col cols="12">
              <v-row>
                <v-col cols="12">
                  <span class="display-1">{{$t('lunch')}} </span> <span class="display-1"><!--<v-btn text icon color="red" @click="removeLunch()"><v-icon>mdi-delete</v-icon></v-btn>--></span>
                </v-col>
                <v-col cols="6">
                  <v-text-field v-model="editDialog.user.lunchFrom" type="time" prefix="Od:"></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field v-model="editDialog.user.lunchTo" type="time" prefix="Do:"></v-text-field>
                </v-col>
              </v-row>
            </v-col>
            <v-col cols="12">
              <v-row>
                <v-col cols="12">
                  <span class="display-1">{{$t('doctor')}}</span> <span class="display-1"><!--<v-btn text icon color="red" @click="removeDoctor()"><v-icon>mdi-delete</v-icon></v-btn>--></span>
                </v-col>
                <v-col cols="6">
                  <v-text-field v-model="editDialog.user.doctorFrom" type="time" prefix="Od:"></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field v-model="editDialog.user.doctorTo" type="time" prefix="Do:"></v-text-field>
                </v-col>
              </v-row>
            </v-col>
            <v-col cols="12">
              <v-row>
                <v-col cols="12">
                  <span class="display-1">{{$t('businessTrip')}}</span> <span class="display-1"><!--<v-btn text icon color="red" @click="removeDoctor()"><v-icon>mdi-delete</v-icon></v-btn>--></span>
                </v-col>
                <v-col cols="6">
                  <v-text-field v-model="editDialog.user.businessFrom" type="time" prefix="Od:"></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field v-model="editDialog.user.businessTo" type="time" prefix="Do:"></v-text-field>
                </v-col>
              </v-row>
            </v-col>
            <v-col cols="12">
              <v-row>
                <v-col cols="12">
                  <span class="display-1">{{$t('privateTrip')}}</span> <span class="display-1"><!--<v-btn text icon color="red" @click="removeDoctor()"><v-icon>mdi-delete</v-icon></v-btn>--></span>
                </v-col>
                <v-col cols="6">
                  <v-text-field v-model="editDialog.user.privateFrom" type="time" prefix="Od:"></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field v-model="editDialog.user.privateTo" type="time" prefix="Do:"></v-text-field>
                </v-col>
              </v-row>
            </v-col>
            <v-col cols="12">
              <v-row>
                <v-col cols="12">
                  <span class="display-1">{{$t('vacation')}}</span> <span class="display-1"><!--<v-btn text icon color="red" @click="removeDoctor()"><v-icon>mdi-delete</v-icon></v-btn>--></span>
                </v-col>
                <v-col cols="6">
                  <v-text-field v-model="editDialog.user.vacationFrom" type="time" prefix="Od:"></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field v-model="editDialog.user.vacationTo" type="time" prefix="Do:"></v-text-field>
                </v-col>
              </v-row>
            </v-col>
          </v-row>
        </v-container>
      </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="blue darken-1"
            text
            @click="closeEdit()"
          >
            {{$t('close')}}
          </v-btn>
          <v-btn
            color="blue darken-1"
            text
            @click="saveEdit()"
          >
            {{$t('save')}}
          </v-btn>
        </v-card-actions>
      </v-card>
      </v-dialog>
      <v-dialog
      v-model="deleteDialog"
      persistent
      max-width="290"
    >
     <v-card>
        <v-card-title class="headline">
          {{$t('reallyDelete')}}?
        </v-card-title>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="green darken-1"
            text
            @click="deleteDialogAgree()"
          >
            {{$t('yes')}}
          </v-btn>
          <v-btn
            color="green darken-1"
            text
            @click="cancelDeleteAttendance()"
          >
            {{$t('no')}}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    </v-row>

    <v-dialog v-model="userInfo" width="1000px">
      <v-card v-if="selectedUser != null">
        <v-card-title>
          <v-row>
            <v-col cols="12">
              <h2>Osobný výkaz</h2>
            </v-col>
            <v-col >
              <span>{{selectedUser.user.firstName}} {{selectedUser.user.lastName}}</span> <span class="ml-2">({{selectedUser.user.department.name}})</span>
            </v-col>
            <v-col class="pb-0">
              <span  style="float: right;">E-mail: {{selectedUser.user.email}}</span>
            </v-col>
            <v-col cols="12" class="pt-0">
              <span>ID: {{selectedUser.user.id}}</span>
            </v-col>
          </v-row>
        </v-card-title>
        <v-card-text>
          <v-data-table
          :headers="selectedUserHeader"
          :items="selectedUser.attendance"
          :items-per-page="200"
          class="elevation-1"
        >
        <template v-slot:[`item.dateAt`]="{ item }">
            <span>{{ (getDayName(item.dateAt)) }}</span>
          </template>
          <template v-slot:[`item.arrivalAt`]="{ item }">
            <span v-if="item.arrivalAt != null">{{
              item.arrivalAt | moment("DD.MM.YYYY HH:mm")
            }}</span>
          </template>
          <template v-slot:[`item.leaveAt`]="{ item }">
            <span v-if="item.leaveAt != null">{{
              item.leaveAt | moment("DD.MM.YYYY HH:mm")
            }}</span>
          </template>
          <template v-slot:[`item.lunchAt`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-if="item.lunchFrom != null" v-bind="attrs" v-on="on">{{$t('yes')}}</span>
                <span v-else >{{$t('no')}}</span>
              </template>
              <span v-if="item.lunchFrom != null">{{item.lunchFrom | moment("HH:mm")}} -</span><span v-if="item.lunchTo != null">{{item.lunchTo | moment("HH:mm")}}</span>
            </v-tooltip>
          </template>
          <template v-slot:[`item.doctor`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-if="item.doctorFrom != null" v-bind="attrs" v-on="on">{{$t('yes')}}</span>
                <span v-else >{{$t('no')}}</span>
              </template>
              <span v-if="item.doctorFrom != null">{{item.doctorFrom | moment("HH:mm")}} -</span><span v-if="item.doctorTo != null">{{item.doctorTo | moment("HH:mm")}}</span>
            </v-tooltip>
          </template>
          <template v-slot:[`item.businessFrom`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-if="item.businessFrom != null" v-bind="attrs" v-on="on">{{$t('yes')}}</span>
                <span v-else >{{$t('no')}}</span>
              </template>
              <span v-if="item.businessFrom != null">{{item.businessFrom | moment("HH:mm")}} -</span><span v-if="item.businessTo != null">{{item.businessTo | moment("HH:mm")}}</span>
            </v-tooltip>
          </template>
          <template v-slot:[`item.privateFrom`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-if="item.privateFrom != null" v-bind="attrs" v-on="on">{{$t('yes')}}</span>
                <span v-else >{{$t('no')}}</span>
              </template>
              <span v-if="item.privateFrom != null">{{item.privateFrom | moment("HH:mm")}} -</span><span v-if="item.privateTo != null">{{item.privateTo | moment("HH:mm")}}</span>
            </v-tooltip>
          </template>
          <template v-slot:[`item.vacationFrom`]="{ item }">
            <v-tooltip bottom>
              <template v-slot:activator="{ on, attrs }">
                <span v-if="item.vacationFrom != null" v-bind="attrs" v-on="on">{{$t('yes')}}</span>
                <span v-else >{{$t('no')}}</span>
              </template>
              <span v-if="item.vacationFrom != null">{{item.vacationFrom | moment("HH:mm")}} -</span><span v-if="item.vacationTo != null">{{item.vacationTo | moment("HH:mm")}}</span>
            </v-tooltip>
          </template>
          <template v-slot:[`item.complet`]="{ item }">
            <span> {{ completedTime(item.arrivalAt, item.leaveAt) }}</span>
          </template>
          <template v-slot:[`item.worked`]="{ item }">
            <span v-if="item.doctor != null"> {{ workedTime(item) }}</span>
            <span v-else> {{ workedTime(item) }}</span>
          </template>
          </v-data-table>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>

export default {
  name: 'OverviewOfApproaches',
  components: {
  },
  data() {
    return {
      attendance: [],
      departments: [],
      editDialog: {
        isOpen: false,
        user: {},
        arrivalModalDate: false,
        arrivalModalTime: false,
        leaveModalDate: false,
        leaveModalTime: false,
      },
      deleteDialog: false,
      attendanceForDelete: null,
      userInfo: false,
      selectedUser: null
    };
  },
  mounted() {
    this.$store.dispatch('setToolbar', "administration.overviewOfApproaches");
    this.getAttendance();
  },
  methods: {
    getAttendance() {
      this.$axios
        .get("/api/attendance/")
        .then((response) => {
          console.log(response);
          this.attendance = response.data;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getDepartments() {
      this.$axios
        .get("/api/department/")
        .then((response) => {
          console.log(response);
          this.departments = response.data;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    editDialogOpen(user) {
      console.log(user);
      this.editDialog.user = JSON.parse(JSON.stringify(user));
      // Parse date and time for better format

      // ARRIVAL
      if(this.editDialog.user.arrivalAt != null) {
        this.editDialog.user.arrivalAt = this.$moment(this.editDialog.user.arrivalAt).format('YYYY-MM-DD HH:mm:ss').split(' ');
      }
      // LEAVE
      if(this.editDialog.user.leaveAt != null) {
      this.editDialog.user.leaveAt = this.$moment(this.editDialog.user.leaveAt).format('YYYY-MM-DD HH:mm:ss').split(' ');
      }
      else {
        this.editDialog.user.leaveAt= [];
        this.editDialog.user.leaveAt[0] = new Date().toISOString().substr(0, 10);
        this.editDialog.user.leaveAt[1] = this.$moment(new Date().toISOString()).format("HH:mm:ss");
      }
      // LUNCH
      if(this.editDialog.user.lunchAt != null) {
        this.editDialog.user.lunchAt = this.$moment(this.editDialog.user.lunchAt).format('YYYY-MM-DD HH:mm:ss').split(' ');
      } else {
        this.editDialog.user.lunchAt= [];
        this.editDialog.user.lunchAt[0] = null;
        this.editDialog.user.lunchAt[1] = null;
        this.editDialog.lunchModalTime = null;
      }
      // DOCTOR
      if(this.editDialog.user.doctor != null) {
        this.editDialog.user.doctor.doctorFrom = this.$moment(this.editDialog.user.doctor.doctorFrom).format('YYYY-MM-DD HH:mm:ss').split(' ');
        this.editDialog.user.doctor.doctorTo = this.$moment(this.editDialog.user.doctor.doctorTo).format('YYYY-MM-DD HH:mm:ss').split(' ');
      } else {
        this.editDialog.user.doctor = {};
        this.editDialog.user.doctor.doctorFrom = [];
        this.editDialog.user.doctor.doctorTo = [];
      }

      if(this.editDialog.user != null && this.editDialog.user.user != null) {
        this.editDialog.isOpen = true;
      }
    },
    saveEdit() {
      this.editDialog.isOpen = false;
      console.log("USER",this.editDialog.user);
      var request = JSON.parse(JSON.stringify(this.editDialog.user));
      //var currentDate = this.$moment(request.dateAt).format('YYYY-MM-DD');
      // LEAVE
      if(request.leaveAt[0] != null && request.leaveAt[1] != null) {
        request.leaveAt = this.$moment(request.leaveAt[0] + " " + request.leaveAt[1]);
      }
      // ARRIVAL
      if(request.arrivalAt[0] != null && request.arrivalAt[1] != null) {
        request.arrivalAt = this.$moment(request.arrivalAt[0] + " " + request.arrivalAt[1]);
      }
      if(request.vacationFrom === "") request.vacationFrom = null
      if(request.vacationTo === "") request.vacationTo = null

      if(request.lunchFrom === "") request.lunchFrom = null
      if(request.lunchTo === "") request.lunchTo = null

      if(request.businessFrom === "") request.businessFrom = null
      if(request.businessTo === "") request.businessTo = null

      if(request.privateFrom === "") request.privateFrom = null
      if(request.privateTo === "") request.privateTo = null

      if(request.doctorFrom === "") request.doctorFrom = null
      if(request.doctorTo === "") request.doctorTo = null
      
      console.log("BeforeSave", request)
      console.log("SAVE Response");
      console.log(request);
      // Save new object to server
      this.$axios.put("/api/attendance/", request).then((response) => {
        console.log(response);
        if(response.data != null) {
          var index = this.attendance.findIndex( x => x.id === response.data.id);
          console.log("Index: " + index);
          // Replace saved object
          this.attendance.splice(index, 1, response.data);
        }
      });
    },
    closeEdit() {
      this.editDialog.isOpen = false;
      // Remove values
      this.editDialog.user= {};
    },
    removeLunch() {
      this.editDialog.user.lunchAt[0] = null;
      this.editDialog.user.lunchAt[1] = null;
      this.editDialog.lunchModalTime = null;
    },
    removeDoctor() {
      console.log("Remove doctor")
      this.editDialog.user.doctor.doctorFrom[0] = null;
      this.editDialog.user.doctor.doctorFrom[1] = null;

      this.editDialog.user.doctor.doctorTo[0] = null;
      this.editDialog.user.doctor.doctorTo[1] = null;

      this.editDialog.doctorFromModalTime = null;
      this.editDialog.doctorToModalTime = null;
    },
    deleteAttendance(item) {
      this.attendanceForDelete = item;
      this.deleteDialog = true;
      console.log("delete", item)
    },
    deleteDialogAgree() {
      
      this.$axios.delete("/api/attendance/"+this.attendanceForDelete.id).then((response) => {
        console.log(response);
        if(response.data != null) {
          var index = this.attendance.findIndex( x => x.id === this.attendanceForDelete.id);
          console.log("Index: " + index);
          // Replace saved object
          this.attendance.splice(index, 1);
          this.attendanceForDelete = null;
          this.deleteDialog = false;
        }
      });
    },
    cancelDeleteAttendance() {
      this.attendanceForDelete = null;
      this.deleteDialog = false;
    },
    completedTime(time1, time2) {
      var date1 = this.$moment(time1);
      var date2 = this.$moment(time2);
      var minutesDiff = date2.diff(date1, 'minutes');

      var hours = (minutesDiff / 60);
      var rhours = Math.floor(hours);
      var minutes = (hours - rhours) * 60;
      var rminutes = Math.round(minutes);
      return rhours + "h "+ rminutes +"m";
    },
    workedTime(attendance) {
      //console.log("attendance", attendance);
      var lunchDiff = null;
      var doctorDiff = null;
      var businessDiff = null;
      var privateDiff = null;
      var vacationDiff = null;
      var workFrom = this.$moment(attendance.arrivalAt);
      var workTo = this.$moment(attendance.leaveAt);
      var minutesDiff = workTo.diff(workFrom, 'minutes');
      if(attendance.lunchFrom != null && attendance.lunchTo != null) {
        var lunchFrom = this.$moment(attendance.lunchFrom, "HH:mm");
        var lunchTo = this.$moment(attendance.lunchTo, "HH:mm");
        lunchDiff = lunchTo.diff(lunchFrom, 'minutes');
        minutesDiff = minutesDiff-lunchDiff;
        console.log("lunchDiff", lunchDiff)
      }
      if(attendance.doctorFrom != null && attendance.doctorTo != null) {
          var doctorFrom = this.$moment(attendance.doctorFrom, "HH:mm");
          var doctorTo = this.$moment(attendance.doctorTo, "HH:mm");
          doctorDiff = doctorTo.diff(doctorFrom, 'minutes');
          minutesDiff = minutesDiff - doctorDiff;
          console.log('doctorFrom', doctorFrom)
          console.log("DoctorDiff", doctorDiff)
      }
      if(attendance.businessFrom != null && attendance.businessTo != null) {
          var businessFrom = this.$moment(attendance.businessFrom, "HH:mm");
          var businessTo = this.$moment(attendance.businessTo, "HH:mm");
          businessDiff = businessTo.diff(businessFrom, 'minutes');
          minutesDiff = minutesDiff - businessDiff;
          console.log("businessDiff", businessDiff)
      }
      if(attendance.privateFrom != null && attendance.privateTo != null) {
          var privateFrom = this.$moment(attendance.privateFrom, "HH:mm");
          var privateTo = this.$moment(attendance.privateTo, "HH:mm");
          privateDiff = privateTo.diff(privateFrom, 'minutes');
          minutesDiff = minutesDiff - privateDiff;
          console.log("privateDiff", privateDiff)
      }
      if(attendance.vacationFrom != null && attendance.vacationTo != null) {
          var vacationFrom = this.$moment(attendance.vacationFrom, "HH:mm");
          var vacationTo = this.$moment(attendance.vacationTo, "HH:mm");
          vacationDiff = vacationTo.diff(vacationFrom, 'minutes');
          minutesDiff = minutesDiff - vacationDiff;
          console.log("vacationDiff", vacationDiff)
      }
      if(minutesDiff < 0) {
          return "0h 0m";
      }
      var hours = (minutesDiff / 60);
      var rhours = Math.floor(hours);
      var minutes = (hours - rhours) * 60;
      var rminutes = Math.round(minutes);
      return rhours + "h "+ rminutes +"m";
    },
    getDayName(date) {
      var dt = this.$moment(date);
      
      return this.$t(dt.format('dddd'));
    },
    async userInfoModal(userId){
      console.log("Click", userId)
      await this.$axios.get("/api/attendance/user/"+userId).then((response) => {
        console.log("Attendance For User", response);
        if(response.status === 200 ) {
          this.selectedUser = response.data
          this.userInfo = true
        }
      });
    }
  },
  computed: {
      attendanceHeader() {
        return [
        {text: this.$t("name"), align: "start", sortable: true, value: "user",},
        { text: this.$t("department"), value: "department" },
        { text: this.$t("arrival"), value: "arrivalAt" },
        { text: this.$t("leaving"), value: "leaveAt" },
        { text: this.$t("lunch"), value: "lunchFrom" },
        { text: this.$t("doctor"), value: "doctor"},
        { text: this.$t('homePage.businessTrip'), value: 'businessFrom'},
        { text: this.$t('homePage.privateTrip'), value: 'privateFrom'},
        { text: this.$t('homePage.vacation'), value: 'vacationFrom'},
        { text: this.$t("actions"), value: "actions"}
      ]
      },
      selectedUserHeader() {
        return [
        { text: this.$t("day"), value: "dateAt" },
        { text: this.$t("arrival"), value: "arrivalAt" },
        { text: this.$t("leaving"), value: "leaveAt" },
        { text: this.$t("lunch"), value: "lunchAt" },
        { text: this.$t("doctor"), value: "doctor" },
        { text: this.$t('homePage.businessTrip'), value: 'businessFrom'},
        { text: this.$t('homePage.privateTrip'), value: 'privateFrom'},
        { text: this.$t('homePage.vacation'), value: 'vacationFrom'},
        { text: this.$t("complet"), value: "complet"},
        { text: this.$t('worked'), value: "worked"}
      ]
      }
  },
}
</script>