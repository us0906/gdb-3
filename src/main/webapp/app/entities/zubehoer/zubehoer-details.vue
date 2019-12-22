<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="zubehoer">
                <h2 class="jh-entity-heading"><span v-text="$t('gdb3App.zubehoer.detail.title')">Zubehoer</span> {{zubehoer.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span v-text="$t('gdb3App.zubehoer.bezeichnung')">Bezeichnung</span>
                    </dt>
                    <dd>
                        <span>{{zubehoer.bezeichnung}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.zubehoer.gueltigBis')">Gueltig Bis</span>
                    </dt>
                    <dd>
                        <span>{{zubehoer.gueltigBis}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.zubehoer.hersteller')">Hersteller</span>
                    </dt>
                    <dd>
                        <div v-if="zubehoer.herstellerId">
                            <router-link :to="{name: 'HerstellerView', params: {herstellerId: zubehoer.herstellerId}}">{{zubehoer.herstellerBezeichnung}}</router-link>
                        </div>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.zubehoer.zubehoerTyp')">Zubehoer Typ</span>
                    </dt>
                    <dd>
                        <div v-if="zubehoer.zubehoerTypId">
                            <router-link :to="{name: 'ZubehoerTypView', params: {zubehoerTypId: zubehoer.zubehoerTypId}}">{{zubehoer.zubehoerTypBezeichnung}}</router-link>
                        </div>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
                </button>
                <router-link v-if="zubehoer.id" :to="{name: 'ZubehoerEdit', params: {zubehoerId: zubehoer.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./zubehoer-details.component.ts">
</script>
