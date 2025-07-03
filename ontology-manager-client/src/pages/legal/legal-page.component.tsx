
import { Accordion, AccordionTab } from 'primereact/accordion';
import { PageTitle } from '../../components';
import { toolDisclaimer } from '../../utils';
        
const React = require('react');

const LegalPageComponent: React.FC = () => {
    return <div className='page-container-narrow'>
    
    <PageTitle icon='pi pi-book' title='Legal Information' help={false}></PageTitle>
    
    <Accordion multiple activeIndex={[]}>
             
    <AccordionTab header="Imprint">
        <p>
        The Natural-One-Health service (n1h.org, naturalonehealth.org) is developed and operated by the<br/><br/>


        <a href="https://www.ipb-halle.de">Leibniz Institute of Plant Biochemistry</a><br/>
        Leibniz-Institute of Plant Biochemistry (IPB)<br/>
        Weinberg 3<br/>
        06120 Halle (Saale)<br/>
        GERMANY<br/>
        <br/>
        The IPB is a foundation under public law.<br/>
        <br/>
        The persons to represent the institute are:<br/>
        Prof. Dr. Alain Tissier (Managing Director)<br/>
        Peter Zuber (Administrative Head)<br/>
        </p>
        <p><b>Supervisory authority</b><br/>
        Ministerium für Wissenschaft, Energie, <br/>
        Klimaschutz und Umwelt des Landes Sachsen-Anhalt<br/>
        Leipziger Straße 58<br/>
        39112 Magdeburg<br/>
        Deutschland<br/>
        </p>
        <p><b>VAT-ID</b><br/>
        DE-811639130<br/>
        </p>

        <p>
        <b>Contact</b><br/>
            Tel: +49-345-5582-0<br/>
            Fax: +49-345-5582-1009<br/>
            Email: pr@ipb-halle.de<br/>
            WWW: <a href="https://www.ipb-halle.de">www.ipb-halle.de</a><br/>
        </p>
        <p><b>For specific questions regarding N1H.org or this software, please address the developers 
        directly (fbroda@ipb-halle.org, Tel. +49-345-5582-1361).</b>
        </p>
    </AccordionTab>
    <AccordionTab header="Privacy / GDPR Declaration">
        <div className="text-container">
            <h1>Privacy policy / GDPR declaration</h1>
            <p><i>Please note: This text is an automatic translation from a German original text. Its purpose is to inform you about 
            your rights - not to deny any rights to you. This declaration applies to N1H.org, naturalonehealth.org and their sub-domains (hereinafter N1H.org).</i></p>

            <h2>Responsible Body</h2>
            <p>Responsible body for the purpose of the General Data Protection Regulation and other national data protection laws of the 
            EU member states as well as other data and privacy protection regulations is 

            <center>
            Leibniz Institute of Plant Biochemistry<br/>
            Weinberg 3<br/>
            06120 Halle (Saale)<br/>
            GERMANY<br/>
            <br/>
            Data Protection Officer: <a href="mailto:datenschutz@ipb-halle.de">datenschutz@ipb-halle.de</a><br/>
            </center>
            </p>
        </div>

        <div className="text-container">
            <h2>General information on data processing</h2>
            <p>N1H.org is aimed at the interested public (scientists, technical staff, students, etc.). The offer 
            can be used anonymously - there is no mechanism for registration or personalization. Even with anonymous use, however, 
            the processing of personal information (namely IP address, time of call, operating system and browser version, URL of 
            the requested resource and referrer) takes place. The temporary storage and processing of this data is 
            necessary for the delivery of the content as well as the technical monitoring of the systems. Obtaining your prior 
            consent to this data processing is regularly actually not possible. The legal basis for the processing and storage 
            of this data is Art. 6 para. 1 lit. b or f. 
            GDPR.</p>
        </div>

        <div className="text-container">
            <h2>Purpose of data processing</h2>
            <p>The purpose of the data processing is the delivery of the content.</p>
        </div>

        <div className="text-container">
            <h2>Cookies, log files, ...</h2>
            <p>N1H.org does not use cookies. There is no (external) advertising, tracking pixels, browser fingerprinting, 
            analytics or profiling on N1H.org. We do not load resources from external servers. On N1H.org you cannot buy 
            goods or services and you cannot register personally. There is no transfer of personal data to third parties.</p>

            <p>To monitor the servers and to support troubleshooting, anonymized log files are created by the servers. The log 
            files are deleted after 7 days at the latest.</p>

            <p>The N1H.org servers are operated in German data centers.</p>
        </div>

        <div className="text-container">
            <h2>Rights of the data subject</h2>
            <p>If your personal data is processed, you are a data subject within the meaning of the GDPR and you are entitled to 
            the following rights towards the operator:</p>
        </div>

        <div className="text-container">
            <h2>Right to information</h2>
            <p>You may request confirmation from the operator as to whether personal data concerning you is being processed by us. 
            If such processing is taking place, you may request information from the operator about the following:

            <ol>
                <li>The purposes for which the personal data are processed</li>
                <li>the categories of personal data which are processed</li>
                <li>the recipients or categories of recipients to whom the personal data concerning you have been or will be 
                    disclosed</li>
                <li>the planned duration of the storage of the personal data concerning you or, if concrete information on this 
                    is not possible, criteria for determining the storage period</li>
                <li>the existence of a right to rectification or deletion of the personal data concerning you, a right to 
                    restriction of processing by the operator or a right to object to such processing</li>
                <li>the existence of a right of appeal to a supervisory authority</li>
                <li>any available information on the origin of the data, if the personal data are not collected from the data 
                    subject</li>
                <li>the existence of automated decision-making, including profiling, pursuant to Article 22(1) and (4) of the 
                    GDPR and, at least in these cases, meaningful information about the logic involved and the scope and intended effects of 
                    such processing for the data subject.</li>
            </ol></p>

            <p>You have the right to request information about whether the personal data concerning you are transferred to a 
            third country or to an international organization. In this context, you may request to be informed about the 
            appropriate safeguards pursuant to Art. 46 GDPR in connection with the transfer.</p>
        </div>

        <div className="text-container">
            <h2>Right to rectification</h2>
            <p>You have a right to rectification and/or completion towards the operator if the personal data processed 
            concerning you is inaccurate or incomplete. The operator shall carry out the rectification without undue delay.</p>
        </div>

        <div className="text-container">
            <h2>Right to restriction of processing</h2>
            <p>
            Under the following conditions, you may request the restriction of the processing of personal data concerning you:
            <ol>
                <li>if you contest the accuracy of the personal data concerning you for a period enabling the operator to verify 
                    the accuracy of the personal data;</li>
                <li>the processing is unlawful and you object to the deletion of the personal data and request instead the 
                    restriction of the use of the personal data</li>
                <li>the operator no longer needs the personal data for the purposes of processing, but you need them for the 
                    establishment, exercise or defense of legal claims; or</li>
                <li>if you have objected to the processing pursuant to Article 21 (1) GDPR and it is not yet clear whether the 
                    legitimate grounds of the operator outweigh your grounds.</li>
            </ol></p>

            <p>If the processing of personal data concerning you has been restricted, such data may - apart from being stored - 
            only be processed with your consent or for the assertion, exercise or defense of legal claims or for the protection of 
            the rights of another natural or legal person or for reasons of important public interest of the Union or a Member 
            State.</p>

            <p>If the restriction of processing has been restricted in accordance with the above conditions, you will be informed 
            by the operator before the restriction is lifted. In case of data processing for scientific, historical or 
            statistical research purposes.</p>
        </div>

        <div className="text-container">
            <h2>Right to deletion</h2>
            <h3>a) Obligation to delete</h3>
            <p>You may request the operator to delete the personal data concerning you without undue delay, and the operator is 
            obliged to delete such data without undue delay, if one of the following reasons applies:
            <ol>
                <li>The personal data concerning you are no longer necessary for the purposes for which they were collected or 
                    otherwise processed.</li>
                <li>You withdraw your consent on which the processing was based pursuant to Art. 6 (1) a or Art. 9 (2) a GDPR and 
                    there is no other legal basis for the processing.</li>
                <li>You object to the processing pursuant to Art. 21 (1) GDPR and there are no overriding legitimate grounds for 
                    the processing, or you object to the processing pursuant to Art. 21 (2) GDPR.</li>
                <li>The personal data concerning you has been processed unlawfully.</li>
                <li>The deletion of the personal data concerning you is necessary for compliance with a legal obligation under Union 
                    or Member State law to which the operator is subject.</li>
                <li>The personal data concerning you has been collected in relation to information society services offered 
                    pursuant to Article 8(1) of the GDPR.</li>
            </ol></p>

            <h3>b) Information to third parties</h3>
            <p>If the operator has made the personal data concerning you public and is obliged to delete it pursuant to Article 
            17(1) of the GDPR, he shall take reasonable steps, including technical measures, having regard to the available 
            technology and the cost of implementation, to inform third party operators that you as the data subject have 
            requested the deletion of the data and all links to or replications of it.</p>

            <h3>c) Exceptions</h3>
            <p>The right to deletion does not exist to the extent that the processing is necessary
            <ol>
                <li>For the exercise of the right to freedom of expression and information;</li>
                <li>for compliance with a legal obligation which requires processing under Union or Member State law to which the 
                    operator is subject, or for the performance of a task carried out in the public interest or in the exercise of 
                    official authority vested in the operator;</li>
                <li>for reasons of public interest in the area of public health pursuant to Art. 9(2)(h) and (i) and Art. 9(3) 
                    GDPR;</li>
                <li>for archiving purposes in the public interest, scientific or historical research purposes or statistical 
                    purposes pursuant to Article 89(1) GDPR, insofar as the right referred to in section a) is likely to render impossible 
                    or seriously prejudice the achievement of the purposes of such processing; or</li>
                <li>for the assertion, exercise or defense of legal claims.</li>
            </ol></p>
        </div>

        <div className="text-container">
            <h2>Right to information</h2>
            <p>If you have asserted the right to rectification, deletion or restriction of processing against the operator, the 
            operator is obliged to inform all recipients to whom the personal data concerning you have been disclosed of this 
            rectification or deletion of the data or restriction of processing, unless this proves impossible or involves a 
            disproportionate effort.</p>

            <p>You have the right to be informed about these recipients by the data operator.</p>
        </div>

        <div className="text-container">
            <h2>Right to data portability</h2>
            <p>You have the right to receive the personal data concerning you that you have provided to the operator in a 
            structured, common and machine-readable format. You also have the right to transfer this data to another operator 
            without hindrance from the operator to whom the personal data was provided, provided that
            <ol>
                <li>the processing is based on consent pursuant to Art. 6 (1) a GDPR or Art. 9 (2) a GDPR or on a contract pursuant to 
                    Art. 6 (1) b GDPR and</li>
                <li>the processing is carried out with the help of automated procedures.</li>
            </ol></p>

            <p>In exercising this right, you also have the right to obtain that the personal data concerning you be transferred 
            directly from one operator to another operator, insofar as this is technically feasible. Freedoms and rights of 
            other persons must not be affected by this.</p>

            <p>The right to data portability does not apply to processing of personal data necessary for the performance of a task 
            carried out in the public interest or in the exercise of official authority vested in the operator.</p>
        </div>

        <div className="text-container">
            <h2>Right to object</h2>
            <p><b>You have the right to object at any time</b>, on grounds relating to your particular situation, <b>to the 
            processing of personal data</b> concerning you which is carried out on the basis of Article 6(1)(e) or (f) GDPR; this 
            also applies to profiling based on these provisions.</p>

            <p>The operator shall no longer process the personal data concerning you unless it can demonstrate compelling 
            legitimate grounds for the processing which override your interests, rights and freedoms, or the processing serves the 
            purpose of asserting, exercising or defending legal claims.</p>

            <p>If the personal data concerning you is processed for the purposes of direct marketing, you have the right to object 
            at any time to the processing of personal data concerning you for the purposes of such marketing; this also applies to 
            profiling, insofar as it is related to such direct marketing.</p>

            <p>If you object to the processing for direct marketing purposes, the personal data concerning you will no longer be 
            processed for these purposes.</p>

            <p>You have the possibility, in connection with the use of information society services - notwithstanding Directive 
            2002/58/EC - to exercise your right to object by means of automated procedures using technical specifications.</p>
        </div>

        <div className="text-container">
            <h2>Right to revoke your declaration of consent under data protection law</h2>
            <p><b>You have the right to revoke your declaration of consent under data protection law at any time.</b> The 
            revocation of consent does not affect the lawfulness of the processing carried out on the basis of the consent until 
            the revocation.</p>
        </div>

        <div className="text-container">
            <h2>Automated decision in individual cases including profiling</h2>
            <p>You have the right not to be subject to a decision based solely on automated processing - including profiling - 
            which produces legal effects concerning you or similarly significantly affects you. This does not apply if the decision
            <ol>
                <li>is necessary for the conclusion or performance of a contract between you and the operator,</li>
                <li>is permitted by legal provisions of the Union or the Member States to which the operator is subject and these 
                    legal provisions contain appropriate measures to protect your rights and freedoms as well as your legitimate interests, 
                    or</li>
                <li>is carried out with your explicit consent.</li>
            </ol></p>

            <p>However, these decisions may not be based on special categories of personal data pursuant to Article 9(1) of the 
            GDPR, unless Article 9(2)(a) or (g) applies and appropriate measures have been taken to protect the rights and freedoms 
            and your legitimate interests.</p>

            <p>With regard to the cases referred to in (1) and (3), the operator shall take reasonable steps to safeguard the 
            rights and freedoms as well as your legitimate interests, including at least the right to obtain the intervention of a 
            person on the part of the operator, to express his or her point of view and to contest the decision.</p>
        </div>

        <div className="text-container">
            <h2>Right to complain to a supervisory authority</h2>
            <p>Without prejudice to any other administrative or judicial remedy, you have the right to lodge a complaint with a 
            supervisory authority, in particular in the Member State of your residence, workplace or the place of the alleged 
            infringement, if you consider that the processing of personal data concerning you infringes the GDPR (Art. 77 GDPR).</p>

            <p>The supervisory authority to which the complaint has been lodged will inform the complainant of the status and 
            outcome of the complaint, including the possibility of a judicial remedy under Art. 78 GDPR.</p>

            <p>Supervisory authority to the operator is the state data protection officer of Saxony-Anhalt:<center>
                <a href="https://datenschutz.sachsen-anhalt.de/datenschutz-in-sachsen-anhalt/">https://datenschutz.sachsen-anhalt.de/datenschutz-in-sachsen-anhalt/</a><br/>
            </center>
            </p>
        </div>
        <div className="text-container last">
            <h2>Up-to-dateness of the privacy policy</h2>
            <p>The data protection declaration has been reviewed by the external data protection officer of the Leibniz Institute 
            of Plant Biochemistry (IPB). The data protection declaration is regularly adapted to current changes in the legal 
            situation or due to legal decisions.</p>
            <p>Revision: June, 16<sup>th</sup> 2025</p>
        </div>
    </AccordionTab>
    <AccordionTab header="Accessibility Statement">
        <p>
        N1H.org is primarily targeted at and a tool for international scientists. As such, it is neither appropriate nor possible to adhere to the full extent of accessibility standards.
        Please also note that the N1H.org web application is work in progress. We're currently re-working our code to improve security, add new functionality and to boost accessibility. We need to ask for your 
        patience, because our manpower is limited.
        We have assessed this service according to <a href="https://www.w3.org/TR/WCAG22/">WCAG 2.2</a> and are aware of several accessibility issues in this version of N1H.org. Among them:
        <ul>
        <li>page elements with poor contrasts</li>
        <li>very small font sizes</li>
        <li>missing or mis-leading semantic annotation of page elements</li>
        <li>missing, mis-leading or un-informative alternative descriptions for images</li>
        <li>page elements, which are not operable with the keyboard</li>
        <li>downloadable elements, which do not conform with accessibility standards</li>
        <li>confusing or garbled page structure</li>
        </ul>
        We will try to fix these issues as long as these fixes do not impact the scientific value of N1H.org.<br/>
        </p>
        <p>
        <b>Feedback</b><br/>
        If you wish to report accessibility issues, you can contact us preferrably using the developer contact options given in the imprint. 
        </p>
        <p>
        <b>Enforcement procedure</b><br/>
        If you have reported a barrier to us and we have not given you a satisfactory response from your
        point of view after one month, you can contact the ombudsman's office in accordance with Section 16d
        of the Disability Equality Act of Saxony-Anhalt (BGG LSA) (ombudsstelle@ukst.de). You can also contact
        the ombudsman's office using the telephone and postal contact options of the
        State Office for Accessibility Saxony-Anhalt:<br/>
        <br/>
        <a href="https://www.lf-barrierefreiheit-st.de/">State Office for Accessibility</a><br/>
        Unfallkasse Sachsen-Anhalt<br/>
        Käsperstraße 31<br/>
        39261 Zerbst/Anhalt<br/>
        Phone: 49 3923 751 - 175
        </p>
        <p>
        <b>Topicality</b><br/>
        We have assessed this service in June 2025. Next assessment is due in one year.
       </p>
    </AccordionTab>
    </Accordion>
    
    </div>;
};

export default LegalPageComponent;
