<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="geraet">
                <h2 class="jh-entity-heading"><span v-text="$t('gdb3App.geraet.detail.title')">Geraet</span> {{geraet.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span v-text="$t('gdb3App.geraet.bezeichnung')">Bezeichnung</span>
                    </dt>
                    <dd>
                        <span>{{geraet.bezeichnung}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.geraet.gueltigBis')">Gueltig Bis</span>
                    </dt>
                    <dd>
                        <span>{{geraet.gueltigBis}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.geraet.geraetTyp')">Geraet Typ</span>
                    </dt>
                    <dd>
                        <div v-if="geraet.geraetTypId">
                            <router-link :to="{name: 'GeraetTypView', params: {geraetTypId: geraet.geraetTypId}}">{{geraet.geraetTypBezeichnung}}</router-link>
                        </div>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.geraet.hersteller')">Hersteller</span>
                    </dt>
                    <dd>
                        <div v-if="geraet.herstellerId">
                            <router-link :to="{name: 'HerstellerView', params: {herstellerId: geraet.herstellerId}}">{{geraet.herstellerBezeichnung}}</router-link>
                        </div>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
                </button>
                <router-link v-if="geraet.id" :to="{name: 'GeraetEdit', params: {geraetId: geraet.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./geraet-details.component.ts">
</script>
