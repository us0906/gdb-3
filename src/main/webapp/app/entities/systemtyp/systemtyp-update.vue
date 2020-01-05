<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="gdb3App.systemtyp.home.createOrEditLabel" v-text="$t('gdb3App.systemtyp.home.createOrEditLabel')">Create or edit a Systemtyp</h2>
                <div>
                    <div class="form-group" v-if="systemtyp.id">
                        <label for="id" v-text="$t('global.field.id')">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="systemtyp.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.systemtyp.bezeichnung')" for="systemtyp-bezeichnung">Bezeichnung</label>
                        <input type="text" class="form-control" name="bezeichnung" id="systemtyp-bezeichnung"
                            :class="{'valid': !$v.systemtyp.bezeichnung.$invalid, 'invalid': $v.systemtyp.bezeichnung.$invalid }" v-model="$v.systemtyp.bezeichnung.$model"  required/>
                        <div v-if="$v.systemtyp.bezeichnung.$anyDirty && $v.systemtyp.bezeichnung.$invalid">
                            <small class="form-text text-danger" v-if="!$v.systemtyp.bezeichnung.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.systemtyp.bezeichnung.minLength" v-bind:value="$t('entity.validation.minlength')">
                                This field is required to be at least 1 characters.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.systemtyp.bezeichnung.maxLength" v-bind:value="$t('entity.validation.maxlength')">
                                This field cannot be longer than 200 characters.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.systemtyp.gueltigBis')" for="systemtyp-gueltigBis">Gueltig Bis</label>
                        <div class="input-group">
                            <input id="systemtyp-gueltigBis" type="date" class="form-control" name="gueltigBis"  :class="{'valid': !$v.systemtyp.gueltigBis.$invalid, 'invalid': $v.systemtyp.gueltigBis.$invalid }"
                            v-model="$v.systemtyp.gueltigBis.$model"  />
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="form-control-label" v-text="$t('gdb3App.systemtyp.geraet')" for="systemtyp-geraet">Geraet</label>

                        <v-select label="bezeichnung"
                                  id="systemtyp-geraet"
                                  :options="geraets"
                                  v-model="$v.systemtyp.geraetId.$model" required
                                  :reduce="g => g.id"
                                  :value="selected"
                                  :searchable="true"
                                  :filterable="true"
                                  :clearable="true">
                        </v-select>
                    </div>

                    <div v-if="$v.systemtyp.geraetId.$anyDirty && $v.systemtyp.geraetId.$invalid">
                        <small class="form-text text-danger" v-if="!$v.systemtyp.geraetId.required" v-text="$t('entity.validation.required')">
                            This field is required.
                        </small>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('gdb3App.systemtyp.zubehoer')" for="systemtyp-zubehoer">Zubehoer</label>
                       <v-select label="bezeichnung"
                                  id="systemtyp-zubehoer"
                                  :options="zubehoers"
                                  v-model="systemtyp.zubehoerId" required
                                  :reduce="z => z.id"
                                  :value="selected"
                                  :searchable="true"
                                  :filterable="true"
                                  :clearable="true">
                        </v-select>

                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" :disabled="$v.systemtyp.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./systemtyp-update.component.ts">
</script>
