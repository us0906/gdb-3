<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="gdb3App.geraet.home.createOrEditLabel" v-text="$t('gdb3App.geraet.home.createOrEditLabel')">Create or edit a Geraet</h2>
                <div>
                    <div class="form-group" v-if="geraet.id">
                        <label for="id" v-text="$t('global.field.id')">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="geraet.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.geraet.bezeichnung')" for="geraet-bezeichnung">Bezeichnung</label>
                        <input type="text" class="form-control" name="bezeichnung" id="geraet-bezeichnung"
                            :class="{'valid': !$v.geraet.bezeichnung.$invalid, 'invalid': $v.geraet.bezeichnung.$invalid }" v-model="$v.geraet.bezeichnung.$model"  required/>
                        <div v-if="$v.geraet.bezeichnung.$anyDirty && $v.geraet.bezeichnung.$invalid">
                            <small class="form-text text-danger" v-if="!$v.geraet.bezeichnung.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.geraet.bezeichnung.minLength" v-bind:value="$t('entity.validation.minlength')">
                                This field is required to be at least 1 characters.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.geraet.bezeichnung.maxLength" v-bind:value="$t('entity.validation.maxlength')">
                                This field cannot be longer than 200 characters.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.geraet.gueltigBis')" for="geraet-gueltigBis">Gueltig Bis</label>
                        <div class="input-group">
                            <input id="geraet-gueltigBis" type="date" class="form-control" name="gueltigBis"  :class="{'valid': !$v.geraet.gueltigBis.$invalid, 'invalid': $v.geraet.gueltigBis.$invalid }"
                            v-model="$v.geraet.gueltigBis.$model"  />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.geraet.geraetTyp')" for="geraet-geraetTyp">Geraet Typ</label>
                        <!--
                        <select class="form-control" id="geraet-geraetTyp" name="geraetTyp" v-model="$v.geraet.geraetTypId.$model" required>
                            <option v-if="!geraet.geraetTypId" v-bind:value="null" selected></option>
                            <option v-bind:value="geraetTypOption.id" v-for="geraetTypOption in geraetTyps" :key="geraetTypOption.id">{{geraetTypOption.bezeichnung}}</option>
                        </select>
                        -->
                        <v-select label="bezeichnung"
                                  id="geraet-geraetTyp"
                                  :options="geraetTyps"
                                  v-model="$v.geraet.geraetTypId.$model" required
                                  :reduce="gt => gt.id"
                                  :value="selected"
                                  :searchable="true"
                                  :filterable="true"
                                  :clearable="true">
                        </v-select>
                    </div>
                    <div v-if="$v.geraet.geraetTypId.$anyDirty && $v.geraet.geraetTypId.$invalid">
                        <small class="form-text text-danger" v-if="!$v.geraet.geraetTypId.required" v-text="$t('entity.validation.required')">
                            This field is required.
                        </small>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.geraet.hersteller')" for="geraet-hersteller">Hersteller</label>
                        <!--
                        <select class="form-control" id="geraet-hersteller" name="hersteller" v-model="$v.geraet.herstellerId.$model" required>
                            <option v-if="!geraet.herstellerId" v-bind:value="null" selected></option>
                            <option v-bind:value="herstellerOption.id" v-for="herstellerOption in herstellers" :key="herstellerOption.id">{{herstellerOption.bezeichnung}}</option>
                        </select>
                        -->
                        <v-select label="bezeichnung"
                                  id="geraet-hersteller"
                                  :options="herstellers"
                                  v-model="$v.geraet.herstellerId.$model" required
                                  :reduce="h => h.id"
                                  :value="selected"
                                  :searchable="true"
                                  :filterable="true"
                                  :clearable="true">
                        </v-select>
                    </div>
                    <div v-if="$v.geraet.herstellerId.$anyDirty && $v.geraet.herstellerId.$invalid">
                        <small class="form-text text-danger" v-if="!$v.geraet.herstellerId.required" v-text="$t('entity.validation.required')">
                            This field is required.
                        </small>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.geraet.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./geraet-update.component.ts">
</script>
