<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="systemtyp">
                <h2 class="jh-entity-heading"><span v-text="$t('gdb3App.systemtyp.detail.title')">Systemtyp</span> {{systemtyp.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span v-text="$t('gdb3App.systemtyp.bezeichnung')">Bezeichnung</span>
                    </dt>
                    <dd>
                        <span>{{systemtyp.bezeichnung}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.systemtyp.gueltigBis')">Gueltig Bis</span>
                    </dt>
                    <dd>
                        <span>{{systemtyp.gueltigBis}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.systemtyp.geraet')">Geraet</span>
                    </dt>
                    <dd>
                        <div v-if="systemtyp.geraetId">
                            <router-link :to="{name: 'GeraetView', params: {geraetId: systemtyp.geraetId}}">{{systemtyp.geraetBezeichnung}}</router-link>
                        </div>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.systemtyp.zubehoer')">Zubehoer</span>
                    </dt>
                    <dd>
                        <div v-if="systemtyp.zubehoerId">
                            <router-link :to="{name: 'ZubehoerView', params: {zubehoerId: systemtyp.zubehoerId}}">{{systemtyp.zubehoerBezeichnung}}</router-link>
                        </div>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
                </button>
                <router-link v-if="systemtyp.id" :to="{name: 'SystemtypEdit', params: {systemtypId: systemtyp.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./systemtyp-details.component.ts">
</script>
